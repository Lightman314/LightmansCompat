package io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.menu.trader_storage;

import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.ClaimShopData;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.client.menu.trader_storage.ClaimTradeEditClientTab;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.traders.blockentity.TraderBlockEntity;
import io.github.lightman314.lightmanscurrency.api.traders.menu.storage.ITraderStorageMenu;
import io.github.lightman314.lightmanscurrency.api.traders.menu.storage.TraderStorageTab;
import io.github.lightman314.lightmanscurrency.common.traders.permissions.Permissions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import java.util.List;

public class ClaimTradeEditTab extends TraderStorageTab {

    public ClaimTradeEditTab(@Nonnull ITraderStorageMenu menu) { super(menu); }

    @Nonnull
    @Override
    @OnlyIn(Dist.CLIENT)
    public Object createClientTab(@Nonnull Object screen) { return new ClaimTradeEditClientTab(screen,this); }

    @Override
    public boolean canOpen(Player player) { return true; }

    public boolean canEditTrade() { return this.menu.hasPermission(Permissions.EDIT_TRADES); }
    public boolean canEditRentMode() { return this.menu.hasPermission(Permissions.EDIT_TRADES) && this.menu.getTrader() instanceof ClaimShopData shop && !shop.isActive() && !shop.isLocked(); }

    public void SetActive(boolean active)
    {
        if(this.menu.getTrader() instanceof ClaimShopData shop && this.canEditTrade())
        {
            shop.setActive(active);
            if(this.isClient())
                this.menu.SendMessage(this.builder().setBoolean("SetActive",active));
        }
    }

    public void SetPrice(MoneyValue price)
    {
        if(this.menu.getTrader() instanceof ClaimShopData shop && this.canEditTrade())
        {
            shop.setPrice(price);
            if(this.isClient())
                this.menu.SendMessage(this.builder().setMoneyValue("SetPrice",price));
        }
    }

    public void SetRentMode(boolean rentMode)
    {
        if(this.menu.getTrader() instanceof ClaimShopData shop && this.canEditRentMode())
        {
            shop.setRentMode(rentMode);
            if(this.isClient())
                this.menu.SendMessage(this.builder().setBoolean("SetRentMode",rentMode));
        }
    }

    public boolean canReclaimBlock() { return this.menu.getTrader() instanceof ClaimShopData shop && shop.hasBeenPurchased() && shop.getBlockEntity() != null; }

    public void ReclaimBlock()
    {
        if(this.menu.getTrader() instanceof ClaimShopData shop && shop.hasBeenPurchased())
        {
            TraderBlockEntity<?> be = shop.getBlockEntity();
            if(be != null)
            {
                //Collect the items from the trader
                Level level = be.getLevel();
                assert level != null;
                List<ItemStack> contents = shop.getContents(level,be.getBlockPos(),be.getBlockState(),true);
                for(ItemStack item : contents)
                    ItemHandlerHelper.giveItemToPlayer(this.menu.getPlayer(),item);
                //Destroy the trader
                be.flagAsLegitBreak();
                level.setBlockAndUpdate(be.getBlockPos(),Blocks.AIR.defaultBlockState());
            }
        }
    }

    @Override
    public void receiveMessage(LazyPacketData message) {
        if(message.contains("SetActive"))
            this.SetActive(message.getBoolean("SetActive"));
        if(message.contains("SetPrice"))
            this.SetPrice(message.getMoneyValue("SetPrice"));
        if(message.contains("SetRentMode"))
            this.SetRentMode(message.getBoolean("SetRentMode"));
        if(message.contains("ReclaimBlock"))
            this.ReclaimBlock();
    }

}