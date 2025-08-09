package io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.menu.trader_storage;

import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.ClaimShopData;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.client.menu.trader_storage.ClaimRentClientTab;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.traders.menu.storage.ITraderStorageMenu;
import io.github.lightman314.lightmanscurrency.api.traders.menu.storage.TraderStorageTab;
import io.github.lightman314.lightmanscurrency.common.traders.permissions.Permissions;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class ClaimRentTab extends TraderStorageTab {

    public ClaimRentTab(@Nonnull ITraderStorageMenu menu) { super(menu); }

    @Nonnull
    @Override
    @OnlyIn(Dist.CLIENT)
    public Object createClientTab(@Nonnull Object screen) { return new ClaimRentClientTab(screen,this); }

    @Override
    public boolean canOpen(Player player) {
        if(this.menu.getTrader() instanceof ClaimShopData shop && (shop.isCurrentlyRented() || shop.isRentMode()))
            return this.menu.hasPermission(Permissions.EDIT_TRADES);
        return false;
    }

    public boolean isLocked() {
        if(this.menu.getTrader() instanceof ClaimShopData shop)
            return shop.isActive() || shop.isLocked();
        return true;
    }

    public void SetMaxRentPayments(int limit)
    {
        if(this.menu.getTrader() instanceof ClaimShopData shop && shop.hasPermission(this.menu.getPlayer(),Permissions.EDIT_TRADES))
        {
            shop.setMaxRentPayments(limit);
            if(this.isClient())
                this.menu.SendMessage(this.builder().setInt("ChangeMaxPayments",limit));
        }
    }

    public void SetRentDuration(long duration)
    {
        if(this.menu.getTrader() instanceof ClaimShopData shop && shop.hasPermission(this.menu.getPlayer(),Permissions.EDIT_TRADES))
        {
            shop.setRentDuration(duration);
            if(this.isClient())
                this.menu.SendMessage(this.builder().setLong("ChangeDuration",duration));
        }
    }

    @Override
    public void receiveMessage(LazyPacketData message) {
        if(message.contains("ChangeMaxPayments"))
            this.SetMaxRentPayments(message.getInt("ChangeMaxPayments"));
        if(message.contains("ChangeDuration"))
            this.SetRentDuration(message.getLong("ChangeDuration"));
    }

}