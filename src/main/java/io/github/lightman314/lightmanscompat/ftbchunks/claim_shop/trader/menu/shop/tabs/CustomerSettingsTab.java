package io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.menu.shop.tabs;

import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.ClaimShopData;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.client.menu.shop.tabs.CustomerSettingsClientTab;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.menu.shop.ClaimShopMenu;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.menu.shop.ClaimShopTab;
import io.github.lightman314.lightmanscompat.ftbchunks.util.FTBTeamHelper;
import io.github.lightman314.lightmanscurrency.api.misc.player.PlayerReference;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.common.traders.permissions.Permissions;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CustomerSettingsTab extends ClaimShopTab {

    public CustomerSettingsTab(@Nonnull ClaimShopMenu menu) { super(menu); }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Object createClientTab(Object screen) { return new CustomerSettingsClientTab(screen,this); }

    @Override
    public boolean canOpen(Player player) {
        ClaimShopData shop = this.menu.getTrader();
        return shop != null && shop.isRentMode() && (shop.hasPermission(player, Permissions.OPEN_STORAGE) || (shop.isCurrentlyRented() && shop.getCustomer() != null && FTBTeamHelper.isOnSameTeam(shop.getCustomer(), PlayerReference.of(player),this.isClient())));
    }

    public boolean hasAccess() { return this.canOpen(this.menu.player); }

    public void setAlertTime(long alertTime)
    {
        ClaimShopData shop = this.menu.getTrader();
        if(shop != null && this.hasAccess())
        {
            shop.setRentDueWarning(alertTime);
            if(this.isClient())
                this.menu.SendMessage(this.builder().setLong("ChangeRentWarning",alertTime));
        }
    }

    public void clearAlertCache()
    {
        ClaimShopData shop = this.menu.getTrader();
        if(shop != null && this.hasAccess())
        {
            shop.clearRentWarningGiven();
            if(this.isClient())
                this.menu.SendMessage(this.builder().setFlag("ClearRentWarning"));
        }
    }

    public void EndRentalEarly()
    {
        if(this.isClient())
        {
            this.menu.SendMessage(this.builder().setFlag("EndRentalEarly"));
            return;
        }
        ClaimShopData shop = this.menu.getTrader();
        if(shop != null)
            shop.tryEndRental(this.menu.player);
    }

    @Override
    public void receiveMessage(LazyPacketData message) {
        if(message.contains("ChangeRentWarning"))
            this.setAlertTime(message.getLong("ChangeRentWarning"));
        if(message.contains("ClearRentWarning"))
            this.clearAlertCache();
        if(message.contains("EndRentalEarly"))
            this.EndRentalEarly();
    }

}