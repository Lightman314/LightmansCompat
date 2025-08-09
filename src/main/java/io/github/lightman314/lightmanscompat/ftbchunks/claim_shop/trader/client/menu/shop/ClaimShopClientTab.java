package io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.client.menu.shop;

import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.menu.shop.ClaimShopMenu;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.menu.shop.ClaimShopTab;
import io.github.lightman314.lightmanscurrency.client.gui.easy.tabbed.EasyMenuClientTab;
import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class ClaimShopClientTab<T extends ClaimShopTab> extends EasyMenuClientTab<T,ClaimShopMenu,ClaimShopTab,ClaimShopMenuScreen,ClaimShopClientTab<T>> {

    public ClaimShopClientTab(@Nonnull Object screen, @Nonnull T commonTab) { super(screen, commonTab); }

}
