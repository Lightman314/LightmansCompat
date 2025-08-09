package io.github.lightman314.lightmanscompat.ftbchunks.core;

import io.github.lightman314.lightmanscompat.core.LCompatRegistries;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.menu.shop.ClaimShopMenu;
import io.github.lightman314.lightmanscurrency.common.menus.validation.MenuValidator;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class FTBChunksMenus {

    public static void init() {}

    public static final Supplier<MenuType<ClaimShopMenu>> CLAIM_SHOP_MENU;

    static {
        CLAIM_SHOP_MENU = register("ftb_claim_shop",(id,inventory,buffer) -> new ClaimShopMenu(id,inventory,buffer.readLong(), MenuValidator.decode(buffer)));
    }

    private static <T extends AbstractContainerMenu> Supplier<MenuType<T>> register(String id, IContainerFactory<T> menu)
    {
        return LCompatRegistries.getRegistry(ForgeRegistries.MENU_TYPES).register(id,() -> new MenuType<>(menu, FeatureFlagSet.of()));
    }

}