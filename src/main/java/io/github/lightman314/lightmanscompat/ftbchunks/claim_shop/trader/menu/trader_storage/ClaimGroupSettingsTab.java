package io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.menu.trader_storage;

import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.ClaimShopData;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.client.menu.trader_storage.ClaimGroupSettingsClientTab;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.traders.menu.storage.ITraderStorageMenu;
import io.github.lightman314.lightmanscurrency.api.traders.menu.storage.TraderStorageTab;
import io.github.lightman314.lightmanscurrency.common.traders.permissions.Permissions;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ClaimGroupSettingsTab extends TraderStorageTab {

    public ClaimGroupSettingsTab(@Nonnull ITraderStorageMenu menu) { super(menu); }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Object createClientTab(Object screen) { return new ClaimGroupSettingsClientTab(screen,this); }
    @Override
    public boolean canOpen(Player player) { return this.menu.hasPermission(Permissions.EDIT_SETTINGS); }

    public void setGroup(String group)
    {
        if(this.menu.getTrader() instanceof ClaimShopData shop && shop.hasPermission(this.menu.getPlayer(),Permissions.EDIT_SETTINGS))
        {
            shop.setClaimGroup(group);
            if(this.isClient())
                this.menu.SendMessage(this.builder().setString("ChangeGroup",group));
        }
    }

    public void setLimit(int limit)
    {
        if(this.menu.getTrader() instanceof ClaimShopData shop && shop.hasPermission(this.menu.getPlayer(),Permissions.EDIT_SETTINGS))
        {
            shop.setGroupLimit(limit);
            if(this.isClient())
                this.menu.SendMessage(this.builder().setInt("ChangeLimit",limit));
        }
    }

    @Override
    public void receiveMessage(LazyPacketData message) {
        if(message.contains("ChangeGroup"))
            this.setGroup(message.getString("ChangeGroup"));
        if(message.contains("ChangeLimit"))
            this.setLimit(message.getInt("ChangeLimit"));
    }

}