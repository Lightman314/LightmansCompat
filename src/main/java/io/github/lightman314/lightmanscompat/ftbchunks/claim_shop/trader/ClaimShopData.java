package io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Pair;
import io.github.lightman314.lightmanscompat.LCompat;
import io.github.lightman314.lightmanscompat.api.claimshop.ClaimGroupData;
import io.github.lightman314.lightmanscompat.api.claimshop.IGroupableClaimTrader;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.block.ClaimShopState;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.notifications.LandPurchaseNotification;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.notifications.RentDueNotification;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.notifications.RentExpiredNotification;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.notifications.RentPaymentNotification;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.menu.shop.ClaimShopMenu;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.menu.trader_storage.ClaimGroupSettingsTab;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.menu.trader_storage.ClaimMapTab;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.menu.trader_storage.ClaimTradeEditTab;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.menu.trader_storage.ClaimRentTab;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.settings.ClaimGroupSettings;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.settings.ClaimTradeSettings;
import io.github.lightman314.lightmanscompat.ftbchunks.core.FTBChunksBlocks;
import io.github.lightman314.lightmanscompat.ftbchunks.util.FTBChunksHelper;
import io.github.lightman314.lightmanscompat.ftbchunks.util.FTBTeamHelper;
import io.github.lightman314.lightmanscurrency.api.misc.IEasyTickable;
import io.github.lightman314.lightmanscurrency.api.misc.player.PlayerReference;
import io.github.lightman314.lightmanscurrency.api.misc.world.WorldPosition;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.settings.SettingsNode;
import io.github.lightman314.lightmanscurrency.api.traders.*;
import io.github.lightman314.lightmanscurrency.api.traders.blockentity.TraderBlockEntity;
import io.github.lightman314.lightmanscurrency.api.traders.menu.storage.ITraderStorageMenu;
import io.github.lightman314.lightmanscurrency.api.traders.menu.storage.TraderStorageTab;
import io.github.lightman314.lightmanscurrency.api.traders.permissions.PermissionOption;
import io.github.lightman314.lightmanscurrency.api.traders.trade.TradeData;
import io.github.lightman314.lightmanscurrency.api.upgrades.UpgradeType;
import io.github.lightman314.lightmanscurrency.common.menus.providers.EasyMenuProvider;
import io.github.lightman314.lightmanscurrency.common.menus.validation.MenuValidator;
import io.github.lightman314.lightmanscurrency.common.player.LCAdminMode;
import io.github.lightman314.lightmanscurrency.common.traders.permissions.Permissions;
import io.github.lightman314.lightmanscurrency.common.util.IconData;
import io.github.lightman314.lightmanscurrency.util.MathUtil;
import io.github.lightman314.lightmanscurrency.util.TimeUtil;
import io.github.lightman314.lightmanscurrency.util.VersionUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ClaimShopData extends TraderData implements IEasyTickable, IGroupableClaimTrader {

    public static final int CLAIM_RANGE = 3;
    public static final long MIN_RENT_DURATION = TimeUtil.DURATION_HOUR;
    public static final long MAX_RENT_DURATION = TimeUtil.DURATION_DAY * 30;

    public static final TraderType<ClaimShopData> TYPE = new TraderType<>(VersionUtil.modResource(LCompat.MODID,"ftb_claim_shop"),ClaimShopData::new);

    private final ClaimShopTrade trade = new ClaimShopTrade(this);

    private ClaimShopData() { super(TYPE); }
    public ClaimShopData(Level level, BlockPos pos) {
        super(TYPE, level, pos);
        this.chunkTargets.add(new ChunkPos(pos));
    }

    @Override
    public void PickupTrader(Player player, boolean adminState) {
        //Deny pickup ability if this machine is currently active or locked
        if(this.isActive() || this.isLocked())
            return;
        //Otherwise allow it to be moved
        super.PickupTrader(player,adminState);
    }

    @Override
    protected void registerNodes(Consumer<SettingsNode> builder) {
        super.registerNodes(builder);
        builder.accept(new ClaimTradeSettings(this));
        builder.accept(new ClaimGroupSettings(this));
    }

    @Override
    public IconData getIcon() { return IconData.of(FTBChunksBlocks.CLAIM_SHOP); }

    @Override
    protected boolean allowAdditionalUpgradeType(UpgradeType upgradeType) { return false; }

    @Override
    public boolean canShowOnTerminal() { return false; }

    //Main Settings
    private boolean active = false;
    private MoneyValue price = MoneyValue.empty();
    private final Set<ChunkPos> chunkTargets = new HashSet<>();

    //One-time Purchase Settings
    private PlayerReference customer = null;
    public boolean hasBeenPurchased() { return this.customer != null; }

    //Rent Only Settings
    private boolean rentMode = false;
    private long rentStartTime = 0;
    private long rentDuration = 0;
    private boolean rentWarningGiven = false;
    private long rentDueWarning = 0;
    private int prepaidCount = 0;
    private int maxRentPayments = 30;

    //Nonnull when #isCurrentlyRented = true
    @Nullable
    private PlayerReference renter = null;

    //Group Settings
    private String groupName = "";
    private int groupLimit = 0;

    @Nullable
    public PlayerReference getCustomer()
    {
        if(this.isCurrentlyRented())
            return this.renter;
        return this.customer;
    }

    public boolean canBeActivated() {
        if(this.price.isValidPrice() && this.areClaimsValid())
        {
            //Check renting inputs
            return !this.rentMode || this.rentDuration >= MIN_RENT_DURATION;
        }
        return false;
    }
    public boolean isActive() { return this.active; }
    public void setActive(boolean active)
    {
        if(active == this.active)
            return;
        if(active && !this.canBeActivated())
            return;
        this.active = active;
        this.markDirty(this::saveActiveState);
        //Update the block state after changing the active state
        this.updateBlockState();
    }

    public MoneyValue getPrice() { return this.price; }
    public void setPrice(MoneyValue price) {
        this.price = price;
        this.markDirty(this::savePrice);
    }

    public boolean isRentMode() { return this.rentMode; }
    public void setRentMode(boolean rentMode)
    {
        //Cannot disable rent mode if already active
        if(this.rentMode == rentMode || this.active || this.isLocked())
            return;
        this.rentMode = rentMode;
        this.markDirty(this::saveRentDataPacket);
    }

    public Set<ChunkPos> getChunkTargets() { return ImmutableSet.copyOf(this.chunkTargets); }
    public void overrideChunkTargets(Set<ChunkPos> chunks)
    {
        this.chunkTargets.clear();
        this.chunkTargets.addAll(chunks);
        this.validateChunkSelections();
        this.markDirty(this::saveChunks);
    }
    public void addChunkTarget(ChunkPos chunk)
    {
        //Cannot be changed if rented or already purchased
        if(this.isLocked() || isChunkOutOfRange(new ChunkPos(this.getWorldPosition().getPos()),chunk) || this.chunkTargets.contains(chunk))
            return;
        this.chunkTargets.add(chunk);
        this.markDirty(this::saveChunks);
    }
    public void removeChunkTarget(ChunkPos chunk)
    {
        //Cannot be change if rented or already purchased
        if(!this.chunkTargets.contains(chunk) || this.isLocked())
            return;
        this.chunkTargets.remove(chunk);
        this.markDirty(this::saveChunks);
    }

    //Rent Settings
    public int getPrepaidCount() { return this.prepaidCount; }
    public int getMaxRentPayments() { return this.maxRentPayments; }
    public void setMaxRentPayments(int newLimit)
    {
        //Allow this to be changed when activated or rented
        if(this.maxRentPayments == newLimit)
            return;
        this.maxRentPayments = Math.clamp(newLimit,1,99);
        this.markDirty(this::saveRentDataPacket);
    }

    public long getRentDuration() { return this.rentDuration; }
    public void setRentDuration(long newDuration)
    {
        if(this.rentDuration == newDuration || this.isLocked())
            return;
        this.rentDuration = Math.min(newDuration,MAX_RENT_DURATION);
        this.markDirty(this::saveRentDataPacket);
    }

    public long getRentDueWarning() { return this.rentDueWarning; }
    public void setRentDueWarning(long newDuration)
    {
        if(this.rentDueWarning == newDuration)
            return;
        this.rentDueWarning = newDuration;
        this.markDirty(this::saveRentDataPacket);
    }

    public boolean getRentWarningGiven() { return this.rentWarningGiven; }
    public void clearRentWarningGiven() {
        if(!this.rentWarningGiven)
            return;
        this.rentWarningGiven = false;
        this.markDirty(this::saveRentDataPacket);
    }

    @Override
    public String getClaimGroup() { return this.groupName; }
    public void setClaimGroup(String groupName)
    {
        if(groupName.length() > 16)
            groupName = groupName.substring(0,16);
        if(groupName.equals(this.groupName))
            return;
        String oldGroup = this.groupName;
        this.groupName = groupName;
        ClaimGroupData.updateCache(this,oldGroup);
        this.markDirty(this::saveGroupData);
    }

    public int getGroupLimit() { return this.groupName.isBlank() ? 0 : this.groupLimit; }
    public boolean exceedsGroupLimit(PlayerReference player) { return !this.groupName.isBlank() && this.groupLimit > 0 && ClaimGroupData.getCountForGroup(this,player) >= this.groupLimit; }
    public void setGroupLimit(int groupLimit)
    {
        if(this.groupLimit == groupLimit)
            return;
        this.groupLimit = Math.max(0,groupLimit);
        this.markDirty(this::saveGroupData);
    }

    @Override
    public int getTradeCount() { return 1; }

    @Override
    public int getTradeStock(int tradeIndex) { return this.active ? 1 : 0; }

    public boolean isLocked() { return this.hasBeenPurchased() || this.isCurrentlyRented(); }
    public boolean isCurrentlyRented() { return this.renter != null && (this.prepaidCount > 0 || !this.rentTimerExpired()); }
    private boolean rentTimerExpired() {
        if(this.renter != null)
            return !TimeUtil.compareTime(this.rentDuration,this.rentStartTime);
        return false;
    }
    public long rentTimeRemaining()
    {
        if(this.isCurrentlyRented())
        {
            long totalDuration = this.rentDuration + (this.prepaidCount * this.rentDuration);
            return Math.max(0,totalDuration + this.rentStartTime - TimeUtil.getCurrentTime());
        }
        return 0;
    }
    public boolean canEndRental(Player player)
    {
        if(!this.isRentMode() || this.renter == null)
            return false;
        return LCAdminMode.isAdminPlayer(player) || FTBTeamHelper.isOnSameTeam(this.renter,PlayerReference.of(player),this.isClient());
    }
    public void tryEndRental(Player player)
    {
        if(this.isClient() || !this.canEndRental(player))
            return;
        //Trigger the Rent Ending Process as though the timer had expired
        this.onRentExpired();
        //Flag the rent data as changed
        this.markDirty(this::saveRentDataPacket);
    }
    public PlayerReference getIntendedLandOwner()
    {
        if(this.isCurrentlyRented())
        {
            assert this.renter != null;
            return this.renter;
        }
        return this.getOwner().getValidOwner().asPlayerReference();
    }
    public boolean areClaimsValid()
    {
        if(this.chunkTargets.isEmpty() || this.hasBeenPurchased())
            return false;
        PlayerReference intendedOwner = this.getIntendedLandOwner();
        ResourceKey<Level> level = this.getWorldPosition().getDimension();
        if(level == null)
            return false;
        ChunkPos centerPos = new ChunkPos(this.getPos());
        for(ChunkPos pos : this.chunkTargets)
        {
            if(isChunkOutOfRange(centerPos, pos) ||
                    !FTBChunksHelper.isChunkOwner(intendedOwner,this.getWorldPosition().getDimension(),pos,this.isClient()))
                return false;
        }
        return true;
    }

    public List<Pair<ResourceKey<Level>,ChunkPos>> getLockedClaims()
    {
        List<Pair<ResourceKey<Level>,ChunkPos>> results = new ArrayList<>();
        if(this.active || this.isCurrentlyRented())
        {
            ResourceKey<Level> level = this.getWorldPosition().getDimension();
            if(level == null)
                return results;
            for(ChunkPos pos : this.chunkTargets)
                results.add(Pair.of(level,pos));
        }
        return results;
    }

    public static boolean isChunkLocked(ResourceKey<Level> level, ChunkPos pos)
    {
        return TraderAPI.API.GetAllTraders(false).stream().anyMatch(t -> {
            if(t instanceof ClaimShopData shop)
            {
                return shop.getLockedClaims().stream().anyMatch(pair ->
                        pair.getFirst().location().equals(level.location()) && pair.getSecond().equals(pos));
            }
            return false;
        });
    }

    public void reclaimLand(PlayerReference intendedOwner, @Nullable PlayerReference oldOwner)
    {
        if(this.isClient())
            return;
        ResourceKey<Level> level = this.getWorldPosition().getDimension();
        if(level == null)
            return;
        for(ChunkPos pos : this.chunkTargets)
        {
            if(!FTBChunksHelper.tryClaimChunk(intendedOwner,level,pos,oldOwner))
                this.active = false;
        }
        //Flag active status as "changed" if the chunk was deactivated
        if(!this.active)
        {
            this.markDirty(this::saveActiveState);
            this.updateBlockState();
        }

    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {

        //Active state
        this.saveActiveState(tag);
        //Price
        this.savePrice(tag);
        //Chunk Targets
        this.saveChunks(tag);
        //Customer
        this.saveCustomer(tag);
        //Rent Data
        this.saveRentData(tag);
        //Group Settings
        this.saveGroupData(tag);

    }

    protected void saveActiveState(CompoundTag tag) { tag.putBoolean("Active",this.active); }

    protected void saveCustomer(CompoundTag tag) {
        if(this.customer != null)
            tag.put("Customer",this.customer.save());
    }

    protected void savePrice(CompoundTag tag) { tag.put("Price",this.price.save()); }

    @Override
    protected void saveTrades(CompoundTag tag, HolderLookup.Provider provider) { }

    protected void saveChunks(CompoundTag tag) {
        ListTag list = new ListTag();
        for(ChunkPos pos : new ArrayList<>(this.chunkTargets))
            list.add(LongTag.valueOf(pos.toLong()));
        tag.put("Chunks",list);
    }

    protected void saveRentData(CompoundTag tag)
    {
        tag.putBoolean("RentMode",this.rentMode);
        tag.putLong("RentDuration",this.rentDuration);
        tag.putLong("RentDueWarning",this.rentDueWarning);
        tag.putBoolean("RentWarningGiven",this.rentWarningGiven);
        tag.putInt("PrepaidRent",this.prepaidCount);
        tag.putInt("MaxRentPayments",this.maxRentPayments);
        tag.putLong("RentStart",this.rentStartTime);
        if(this.renter != null)
            tag.put("Renter",this.renter.save());
    }

    protected void saveRentDataPacket(CompoundTag tag)
    {
        this.saveRentData(tag);
        if(this.renter == null)
            tag.putBoolean("NoRenter",true);
    }

    protected void saveGroupData(CompoundTag tag)
    {
        tag.putString("ClaimGroup",this.groupName);
        tag.putInt("GroupLimit",this.groupLimit);
    }

    @Override
    protected void saveAdditionalToJson(JsonObject jsonObject, HolderLookup.Provider provider) { }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        if(tag.contains("Active"))
            this.active = tag.getBoolean("Active");
        if(tag.contains("Price"))
            this.price = MoneyValue.load(tag.getCompound("Price"));
        if(tag.contains("Chunks"))
        {
            this.chunkTargets.clear();
            ListTag list = tag.getList("Chunks",Tag.TAG_LONG);
            for (Tag value : list) {
                if (value instanceof NumericTag e)
                    this.chunkTargets.add(new ChunkPos(e.getAsLong()));
            }
        }
        if(tag.contains("Customer"))
            this.customer = PlayerReference.load(tag.getCompound("Customer"));
        if(tag.contains("RentMode"))
            this.rentMode = tag.getBoolean("RentMode");
        if(tag.contains("RentDuration"))
            this.rentDuration = tag.getLong("RentDuration");
        if(tag.contains("RentDueWarning"))
            this.rentDueWarning = tag.getLong("RentDueWarning");
        if(tag.contains("RentWarningGiven"))
            this.rentWarningGiven = tag.getBoolean("RentWarningGiven");
        if(tag.contains("PrepaidRent"))
            this.prepaidCount = tag.getInt("PrepaidRent");
        if(tag.contains("MaxRentPayments"))
            this.maxRentPayments = MathUtil.clamp(tag.getInt("MaxRentPayments"),1,99);
        if(tag.contains("RentStart"))
            this.rentStartTime = tag.getLong("RentStart");
        if(tag.contains("Renter"))
            this.renter = PlayerReference.load(tag.getCompound("Renter"));
        else if(tag.contains("NoRenter"))
            this.renter = null;
        if(tag.contains("ClaimGroup")) //Call set method so that the cache is updated
            this.setClaimGroup(tag.getString("ClaimGroup"));
        if(tag.contains("GroupLimit"))
            this.groupLimit = tag.getInt("GroupLimit");
    }

    @Override
    public void OnRegisteredToOffice() { ClaimGroupData.updateCache(this,null); }

    @Override
    protected void loadAdditionalFromJson(JsonObject jsonObject, HolderLookup.Provider provider) throws JsonSyntaxException, ResourceLocationException { }

    @Override
    protected void saveAdditionalPersistentData(CompoundTag compoundTag, HolderLookup.Provider provider) { }

    @Override
    protected void loadAdditionalPersistentData(CompoundTag compoundTag, HolderLookup.Provider provider) { }

    @Override
    protected void getAdditionalContents(List<ItemStack> list) { }

    @Override
    public List<? extends TradeData> getTradeData() { return Lists.newArrayList(this.trade); }
    public ClaimShopTrade getTrade() { return this.trade; }
    @Nullable
    @Override
    public TradeData getTrade(int i) { return i == 0 ? this.trade : null; }

    @Override
    public void addTrade(Player player) { }

    @Override
    public void removeTrade(Player player) { }

    @Override
    protected TradeResult ExecuteTrade(TradeContext context, int tradeIndex) {
        if(tradeIndex != 0)
            return TradeResult.FAIL_INVALID_TRADE;
        //Confirm that the trader has a valid state
        ResourceKey<Level> level = this.getWorldPosition().getDimension();
        if(level == null)
            return TradeResult.FAIL_INVALID_TRADE;
        //Deny if the price is invalid or if the shop has been disabled
        if(!this.price.isValidPrice() || !this.active)
            return TradeResult.FAIL_OUT_OF_STOCK;
        //Check Pre-Trade Event
        if(this.runPreTradeEvent(this.trade,context).isCanceled())
            return TradeResult.FAIL_TRADE_RULE_DENIAL;
        PlayerReference owner = this.getOwner().getValidOwner().asPlayerReference();
        //Fail if the current claim status is not valid
        if(!this.areClaimsValid())
            return TradeResult.FAIL_INVALID_TRADE;
        if(this.isCurrentlyRented())
        {
            //Force the player to be on the same team as the current renter
            assert this.renter != null;
            if(!FTBTeamHelper.isOnSameTeam(this.renter,context.getPlayerReference(),false))
                return TradeResult.FAIL_INVALID_TRADE;
            //Check if they're allowed another pre-payment
            if(this.prepaidCount >= this.maxRentPayments)
                return TradeResult.FAIL_OUT_OF_STOCK;
        }
        else
        {
            //Confirm that the customer is capable of claiming the new chunks
            if(!FTBChunksHelper.canClaimChunks(context.getPlayerReference(),level,this.chunkTargets,owner))
                return TradeResult.FAIL_NO_OUTPUT_SPACE;
        }
        //Collect the payment
        MoneyValue price = this.runTradeCostEvent(this.trade,context).getCostResult();
        if(!context.getPayment(price))
            return TradeResult.FAIL_CANNOT_AFFORD;
        MoneyValue taxesPaid = this.addStoredMoney(price,true);
        //If rent mode, handle the rent payment
        if(this.rentMode)
        {
            //Update the prepaid rent count
            if(this.isCurrentlyRented())
            {
                this.prepaidCount++;
                this.markDirty(this::saveRentDataPacket);
            }
            else
            {
                //Otherwise, initialize the rented status
                this.rentStartTime = TimeUtil.getCurrentTime();
                this.renter = context.getPlayerReference();
                //Change the land ownership to the renter
                this.reclaimLand(this.renter,owner);
                this.updateBlockState();
            }
            //Update rent warning status
            if(this.rentTimeRemaining() >= this.rentDueWarning)
                this.rentWarningGiven = false;
            //Push Rent Paid Notification
            this.pushNotification(RentPaymentNotification.of(context.getPlayerReference(),price,taxesPaid,this.rentTimeRemaining(),this.getNotificationCategory()));
            //Mark Rent Data as changed
            this.markDirty(this::saveRentDataPacket);
        }
        else
        {
            this.customer = context.getPlayerReference();
            //If not in rent mode, proceed with the one-time purchase
            this.reclaimLand(context.getPlayerReference(),owner);
            //Push Purchase Notification
            this.pushNotification(LandPurchaseNotification.of(context.getPlayerReference(),price,taxesPaid,this.getNotificationCategory()));
            this.markDirty(this::saveActiveState);
        }
        this.runPostTradeEvent(this.trade,context,price,taxesPaid);
        this.updateBlockState();
        return TradeResult.SUCCESS;
    }

    @Override
    public void handleSettingsChange(Player player, LazyPacketData message) {
        super.handleSettingsChange(player, message);
        //Listen in to owner changes
        if((message.contains("ChangePlayerOwner") || message.contains("ChangeOwner")) && this.hasPermission(player, Permissions.TRANSFER_OWNERSHIP))
        {
            //Validate claims, and if claims aren't valid deactivate the machine
            if(!this.areClaimsValid())
                this.setActive(false);
        }
    }

    @Override
    public boolean canMakePersistent() { return false; }

    @Override
    public void initStorageTabs(ITraderStorageMenu menu) {
        menu.setTab(TraderStorageTab.TAB_TRADE_BASIC,new ClaimTradeEditTab(menu));
        menu.setTab(TraderStorageTab.TAB_TRADE_STORAGE,new ClaimRentTab(menu));
        menu.setTab(TraderStorageTab.TAB_TRADE_ADVANCED,new ClaimMapTab(menu));
        menu.setTab(TraderStorageTab.TAB_TRADE_MISC,new ClaimGroupSettingsTab(menu));
    }

    @Override
    protected void addPermissionOptions(List<PermissionOption> list) { }

    @Override
    public void tick() {
        //Don't tick on the client
        if(this.isClient())
            return;
        if(this.renter != null)
        {
            boolean changed = false;
            //Check if we should give a rent due warning
            long timeRemaining = this.rentTimeRemaining();
            if(!this.rentWarningGiven && timeRemaining > 0 && timeRemaining < this.rentDueWarning)
            {
                this.rentWarningGiven = true;
                //Push Rent Due warning to the renter
                FTBTeamHelper.pushNotificationToRenter(this.renter, RentDueNotification.of(timeRemaining,this.getNotificationCategory()));
                //Flag as changed since the rent warning given data was changed
                changed = true;
            }
            //Put in a while loop just in case the server was down for several rent cycles
            while(this.rentTimerExpired())
            {
                if(this.prepaidCount > 0)
                {
                    //Decrement the prepaid rent count and increment the timer by the rent duration
                    this.prepaidCount--;
                    this.rentStartTime += this.rentDuration;
                    changed = true;
                }
                else
                {
                    this.onRentExpired();
                    changed = true;
                    //Break out of the loop
                    break;
                }
            }
            if(changed)
                this.markDirty(this::saveRentDataPacket);
        }
    }

    protected void onRentExpired()
    {
        //Rent has expired completely, reclaim the land and reset the renting status
        //Claim the chunks back to this traders owner
        this.reclaimLand(this.getOwner().getValidOwner().asPlayerReference(),this.renter);
        //Push a Rent Expired notification
        if(this.renter != null)
            this.pushNotification(RentExpiredNotification.of(this.renter,this.getNotificationCategory()));
        //Reset the renting status
        this.renter = null;
        this.prepaidCount = 0;
        this.rentStartTime = 0;
        //Now that the rental period is over, validate the chunk selections and deselect any invalid inputs
        this.validateChunkSelections();
        //Update the block state
        this.updateBlockState();
    }

    @Override
    protected MenuProvider getTraderMenuProvider(MenuValidator validator) { return new ShopMenuProvider(this.getID(),validator); }

    private record ShopMenuProvider(long traderID, MenuValidator validator) implements EasyMenuProvider
    {
        @Override
        public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) { return new ClaimShopMenu(id,inventory,this.traderID,this.validator); }
    }

    @Override
    public void move(Level level, BlockPos pos) {
        super.move(level, pos);
        this.validateChunkSelections();
    }
    @Override
    public void OnTraderMoved(WorldPosition newPosition) {
        super.OnTraderMoved(newPosition);
        this.validateChunkSelections();
    }

    public static boolean isChunkOutOfRange(ChunkPos center, ChunkPos test) { return Math.abs(test.x - center.x) > CLAIM_RANGE || Math.abs(test.z - center.z) > CLAIM_RANGE; }

    private void validateChunkSelections()
    {
        //Don't mess with the targeted chunks if currently being rented
        if(this.isCurrentlyRented())
        {
            if(!this.active)
                return;
            //Deactivate if any chunk is out of range
            ChunkPos centerPos = new ChunkPos(this.getPos());
            if(this.chunkTargets.stream().anyMatch(pos -> isChunkOutOfRange(centerPos,pos)))
            {
                this.active = false;
                this.markDirty(this::saveActiveState);
            }
            return;
        }

        //Delete all selected chunks too far away from this trader
        ChunkPos centerPos = new ChunkPos(this.getPos());
        //Remove the selection
        int startCount = this.chunkTargets.size();
        this.chunkTargets.removeIf(pos -> isChunkOutOfRange(centerPos, pos));
        if(this.chunkTargets.size() != startCount)
            this.markDirty(this::saveChunks);
        if(this.chunkTargets.isEmpty() && this.active)
        {
            this.active = false;
            this.markDirty(this::saveActiveState);
        }
    }

    public void updateBlockState()
    {
        TraderBlockEntity<?> be = this.getBlockEntity();
        if(be == null)
            return;
        Level level = be.getLevel();
        BlockPos pos = be.getBlockPos();
        BlockState state = be.getBlockState();
        if(level != null && state.hasProperty(ClaimShopState.PROPERTY))
        {
            ClaimShopState newState = ClaimShopState.INACTIVE;
            if(this.isCurrentlyRented())
                newState = ClaimShopState.RENTED;
            else if(this.hasBeenPurchased())
                newState = ClaimShopState.SOLD;
            else if(this.isActive())
                newState = this.isRentMode() ? ClaimShopState.FOR_RENT : ClaimShopState.FOR_SALE;
            level.setBlockAndUpdate(pos,state.setValue(ClaimShopState.PROPERTY,newState));
        }
    }

}
