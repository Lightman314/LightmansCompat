package io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.client.menu.trader_storage;

import io.github.lightman314.lightmanscompat.ftbchunks.FTBChunksText;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.ClaimShopData;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.menu.trader_storage.ClaimTradeEditTab;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.money.input.MoneyValueWidget;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.traders.menu.storage.TraderStorageClientTab;
import io.github.lightman314.lightmanscurrency.client.gui.easy.rendering.Sprite;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.PlainButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyAddonHelper;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyTextButton;
import io.github.lightman314.lightmanscurrency.client.util.IconAndButtonUtil;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.common.util.IconData;
import io.github.lightman314.lightmanscurrency.common.util.IconUtil;
import net.minecraft.network.chat.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

public class ClaimTradeEditClientTab extends TraderStorageClientTab<ClaimTradeEditTab> {

    public ClaimTradeEditClientTab(Object screen, ClaimTradeEditTab commonTab) {
        super(screen, commonTab);
    }

    @Override
    protected void initialize(ScreenArea screenArea, boolean firstOpen) {

        ClaimShopData shop = null;
        if(this.menu.getTrader() instanceof ClaimShopData s)
            shop = s;

        //Active Toggle (disabled if not allowed to make active)
        this.addChild(PlainButton.builder()
                .position(screenArea.pos.offset(30,15))
                .sprite(this::activeToggleSprite)
                .pressAction(() -> this.commonTab.SetActive(!this.isActive()))
                .addon(EasyAddonHelper.activeCheck(this.commonTab::canEditTrade))
                .build());

        //Rent Toggle
        this.addChild(PlainButton.builder()
                .position(screenArea.pos.offset(screenArea.width / 2,15))
                .sprite(this::rentToggleSprite)
                .pressAction(() -> this.commonTab.SetRentMode(!this.isRentMode()))
                .addon(EasyAddonHelper.activeCheck(this.commonTab::canEditRentMode))
                .build());

        //Price Input
        this.addChild(MoneyValueWidget.builder()
                .position(screenArea.pos.offset((screenArea.width / 2) - (MoneyValueWidget.WIDTH / 2),40))
                .startingValue(shop == null ? MoneyValue.empty() : shop.getPrice())
                .valueHandler(this.commonTab::SetPrice)
                .addon(EasyAddonHelper.activeCheck(this.commonTab::canEditTrade))
                .build());

        //Reclaim Button
        this.addChild(EasyTextButton.builder()
                .position(screenArea.pos.offset(30,120))
                .width(screenArea.width - 60)
                .text(FTBChunksText.BUTTON_CLAIM_SHOP_RECLAIM_BLOCK.get())
                .addon(EasyAddonHelper.tooltip(FTBChunksText.TOOLTIP_CLAIM_SHOP_RECLAIM_BLOCK.get()))
                .addon(EasyAddonHelper.activeCheck(this.commonTab::canReclaimBlock))
                .pressAction(this.commonTab::ReclaimBlock)
                .build());

    }

    @Override
    public void renderBG(@Nonnull EasyGuiGraphics gui) {

        //Render Active Toggle Text
        gui.drawString(FTBChunksText.GUI_CLAIM_SHOP_ACTIVE.get(),40, 20,0x404040);

        //Render Rent Mode Label
        gui.drawString(FTBChunksText.GUI_CLAIM_SHOP_RENT_MODE.get(),(screen.getXSize() / 2) + 10, 20, 0x404040);

        //Render Price Label
        gui.drawString(FTBChunksText.GUI_CLAIM_SHOP_PRICE.get(),30, 35, 0x404040);

    }

    private Sprite activeToggleSprite() {
        if(!this.commonTab.canEditTrade())
        {
            //Always make gray if not allowed to edit
            return IconAndButtonUtil.SPRITE_NEUTRAL_TOGGLE(this::isActive).get();
        }
        if(this.menu.getTrader() instanceof ClaimShopData shop)
        {
            if(shop.isActive())
                return IconAndButtonUtil.SPRITE_TOGGLE_ACTIVE;
            if(shop.canBeActivated())
                return IconAndButtonUtil.SPRITE_TOGGLE_INACTIVE;
        }
        //Make uncolored if the machine cannot be activated at this time
        return IconAndButtonUtil.SPRITE_NEUTRAL_TOGGLE_DOWN;
    }

    private Sprite rentToggleSprite() {
        Supplier<Sprite> source = this.commonTab.canEditRentMode() ? IconAndButtonUtil.SPRITE_TOGGLE(this::isRentMode) : IconAndButtonUtil.SPRITE_NEUTRAL_TOGGLE(this::isRentMode);
        return source.get();
    }

    private boolean isActive()
    {
        if(this.menu.getTrader() instanceof ClaimShopData shop)
            return shop.isActive();
        return false;
    }

    private boolean isRentMode()
    {
        if(this.menu.getTrader() instanceof ClaimShopData shop)
            return shop.isRentMode();
        return false;
    }

    @Nonnull
    @Override
    public IconData getIcon() { return IconUtil.ICON_TRADELIST; }

    @Nullable
    @Override
    public Component getTooltip() { return FTBChunksText.TOOLTIP_CLAIM_SHOP_EDIT.get(); }

}
