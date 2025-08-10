package io.github.lightman314.lightmanscompat.ftbchunks.client;

import io.github.lightman314.lightmanscompat.api.claimshop.ClaimGroupData;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.client.menu.trader_storage.ClaimMapClientTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.TraderStorageScreen;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class FTBChunksClientEvents {

    @SubscribeEvent
    public static void onScreenClosed(ScreenEvent.Closing event)
    {
        //Trigger the close action when the screen is closed to reset the map textures and remove them from the texture cache
        if(event.getScreen() instanceof TraderStorageScreen screen && screen.currentTab() instanceof ClaimMapClientTab tab)
            tab.onClose();
    }
    @SubscribeEvent
    public static void onClientDisconnect(ClientPlayerNetworkEvent.LoggingOut event) { ClaimGroupData.clearClientCache(); }

}