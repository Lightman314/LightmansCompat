package io.github.lightman314.lightmanscompat.ftbchunks.client;

import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.client.menu.trader_storage.ClaimMapClientTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.TraderStorageScreen;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

public class FTBChunksClientEvents {

    @SubscribeEvent
    public static void onScreenClosed(ScreenEvent.Closing event)
    {
        //Trigger the close action when the screen is closed to reset the map textures and remove them from the texture cache
        if(event.getScreen() instanceof TraderStorageScreen screen && screen.currentTab() instanceof ClaimMapClientTab tab)
            tab.onClose();
    }

}
