package io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.menu.shop;

import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.ClaimShopData;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.menu.shop.tabs.*;
import io.github.lightman314.lightmanscompat.ftbchunks.core.FTBChunksMenus;
import io.github.lightman314.lightmanscurrency.api.traders.TraderAPI;
import io.github.lightman314.lightmanscurrency.common.menus.tabbed.EasyTabbedMenu;
import io.github.lightman314.lightmanscurrency.common.menus.validation.IValidatedMenu;
import io.github.lightman314.lightmanscurrency.common.menus.validation.MenuValidator;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ClaimShopMenu extends EasyTabbedMenu<ClaimShopMenu,ClaimShopTab> implements IValidatedMenu {

    private final long traderID;
    public long getTraderID() { return this.traderID; }
    @Nullable
    public ClaimShopData getTrader() {
        if(TraderAPI.API.GetTrader(this,this.traderID) instanceof ClaimShopData shop)
            return shop;
        return null;
    }
    private final MenuValidator validator;
    @Override
    public MenuValidator getValidator() { return this.validator; }

    public ClaimShopMenu(int id, Inventory inventory, long traderID, MenuValidator validator) { this(FTBChunksMenus.CLAIM_SHOP_MENU.get(),id,inventory,traderID,validator); }
    protected ClaimShopMenu(MenuType<?> type, int id, Inventory inventory, long traderID, MenuValidator validator) {
        super(type, id, inventory, validator);
        this.traderID = traderID;
        this.validator = validator;

        //Add Player Slots
        for(int y = 0; y < 3; ++y) {
            for(int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(inventory, x + y * 9 + 9, 23 + x * 18, 174 + y * 18));
            }
        }

        for(int x = 0; x < 9; ++x) {
            this.addSlot(new Slot(inventory, x, 23 + x * 18, 232));
        }

        this.initializeTabs();
        ClaimShopData trader = this.getTrader();
        if(trader != null)
            trader.userOpen(this.player);

    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        ClaimShopData trader = this.getTrader();
        if(trader != null)
            trader.userClose(this.player);
    }

    @Override
    protected void registerTabs() {
        this.addTab(new CustomerTab(this));
        this.addTab(new CustomerSettingsTab(this));
    }

    public void clearContainer(Container container) {
        this.clearContainer(this.player, container);
    }

    @Nonnull
    public ItemStack quickMoveStack(@Nonnull Player playerEntity, int index) {
        ItemStack clickedStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            clickedStack = slotStack.copy();
            if (index < 36) {
                if (!this.currentTab().quickMoveStack(slotStack) && !this.moveItemStackTo(slotStack, 36, this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < this.slots.size() && !this.moveItemStackTo(slotStack, 0, 36, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return clickedStack;
    }
}