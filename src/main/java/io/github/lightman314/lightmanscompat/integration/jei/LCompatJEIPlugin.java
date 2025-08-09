package io.github.lightman314.lightmanscompat.integration.jei;

import io.github.lightman314.lightmanscompat.LCompat;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.client.menu.shop.ClaimShopMenuScreen;
import io.github.lightman314.lightmanscurrency.integration.jeiplugin.util.JEIScreenArea;
import io.github.lightman314.lightmanscurrency.util.VersionUtil;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModList;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@JeiPlugin
public class LCompatJEIPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() { return VersionUtil.modResource(LCompat.MODID,"lcompat"); }

    private boolean modLoaded(String modid) { return ModList.get().isLoaded(modid); }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        if(this.modLoaded("ftbchunks"))
            registration.addGuiContainerHandler(ClaimShopMenuScreen.class, JEIScreenArea.create(ClaimShopMenuScreen.class,registration.getJeiHelpers().getIngredientManager()));
    }
}
