package io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.client.menu.shop;

import io.github.lightman314.lightmanscompat.LCompat;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.ClaimShopData;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.menu.shop.ClaimShopMenu;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.menu.shop.ClaimShopTab;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.client.gui.easy.tabbed.EasyTabbedMenuScreen;
import io.github.lightman314.lightmanscurrency.client.gui.util.IWidgetPositioner;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.tab.TabButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyAddonHelper;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.WidgetRotation;
import io.github.lightman314.lightmanscurrency.client.gui.widget.util.LazyWidgetPositioner;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.common.traders.permissions.Permissions;
import io.github.lightman314.lightmanscurrency.common.util.IconUtil;
import io.github.lightman314.lightmanscurrency.network.message.trader.CPacketOpenStorage;
import io.github.lightman314.lightmanscurrency.util.VersionUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.Nonnull;

public class ClaimShopMenuScreen extends EasyTabbedMenuScreen<ClaimShopMenu,ClaimShopTab,ClaimShopMenuScreen> {

    public static final ResourceLocation GUI_TEXTURE = VersionUtil.modResource(LCompat.MODID,"textures/gui/ftbchunks/claim_shop.png");

    public static final int WIDTH = 206;
    public static final int HEIGHT = 256;

    public ClaimShopMenuScreen(@Nonnull ClaimShopMenu menu, @Nonnull Inventory inventory) {
        super(menu, inventory);
        this.resize(WIDTH,HEIGHT);
    }

    @Nonnull
    @Override
    protected IWidgetPositioner getTabButtonPositioner() { return LazyWidgetPositioner.create(this,LazyWidgetPositioner.createTopdown(WidgetRotation.LEFT),TabButton.NEGATIVE_SIZE,0,TabButton.SIZE); }

    private IWidgetPositioner rightEdgePositioner;
    public IWidgetPositioner getRightEdgePositioner() { return this.rightEdgePositioner; }

    @Override
    protected void init(ScreenArea screenArea) {

        this.rightEdgePositioner = this.addChild(LazyWidgetPositioner.create(this,LazyWidgetPositioner.createTopdown(),screenArea.width,0,20));

        this.rightEdgePositioner.addWidget(this.addChild(IconButton.builder()
                .pressAction(() -> new CPacketOpenStorage(this.menu.getTraderID()).send())
                .icon(IconUtil.ICON_STORAGE)
                .addon(EasyAddonHelper.visibleCheck(this::canOpenStorage))
                .addon(EasyAddonHelper.tooltip(LCText.TOOLTIP_TRADER_OPEN_STORAGE))
                .build()));

    }

    private boolean canOpenStorage() {
        ClaimShopData shop = this.menu.getTrader();
        return shop != null && shop.hasPermission(this.menu.player,Permissions.OPEN_STORAGE);
    }

    @Override
    protected void renderBackground(@Nonnull EasyGuiGraphics gui) {

        //Render BG
        gui.renderNormalBackground(GUI_TEXTURE,this);

    }

}