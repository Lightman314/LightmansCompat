package io.github.lightman314.lightmanscompat.ftbchunks.client;

import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.client.menu.shop.ClaimShopMenuScreen;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.menu.shop.ClaimShopMenu;
import io.github.lightman314.lightmanscompat.ftbchunks.core.FTBChunksMenus;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class FTBChunksClientModEvents {

    @SubscribeEvent
    public static void registerScreens(FMLClientSetupEvent event)
    {
        event.enqueueWork(() -> {
            MenuScreens.register(FTBChunksMenus.CLAIM_SHOP_MENU.get(),(MenuScreens.ScreenConstructor<ClaimShopMenu,ClaimShopMenuScreen>)(m, i, t) -> new ClaimShopMenuScreen(m,i));
        });
    }

}
