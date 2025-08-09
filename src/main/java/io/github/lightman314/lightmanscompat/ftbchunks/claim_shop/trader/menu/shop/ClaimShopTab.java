package io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.menu.shop;

import io.github.lightman314.lightmanscurrency.common.menus.tabbed.EasyMenuTab;

import javax.annotation.Nonnull;

public abstract class ClaimShopTab extends EasyMenuTab<ClaimShopMenu,ClaimShopTab> {
    public ClaimShopTab(@Nonnull ClaimShopMenu menu) { super(menu); }
}