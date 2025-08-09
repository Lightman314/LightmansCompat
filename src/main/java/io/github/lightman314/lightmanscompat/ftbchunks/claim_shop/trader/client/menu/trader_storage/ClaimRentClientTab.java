package io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.client.menu.trader_storage;

import io.github.lightman314.lightmanscompat.ftbchunks.FTBChunksText;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.ClaimShopData;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.menu.trader_storage.ClaimRentTab;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.traders.menu.storage.TraderStorageClientTab;
import io.github.lightman314.lightmanscurrency.client.gui.widget.TimeInputWidget;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.PlainButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyAddonHelper;
import io.github.lightman314.lightmanscurrency.client.util.IconAndButtonUtil;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.client.util.TextRenderUtil;
import io.github.lightman314.lightmanscurrency.common.util.IconData;
import io.github.lightman314.lightmanscurrency.common.util.IconUtil;
import io.github.lightman314.lightmanscurrency.util.TimeUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ClaimRentClientTab extends TraderStorageClientTab<ClaimRentTab> {

    public ClaimRentClientTab(Object screen, ClaimRentTab commonTab) { super(screen, commonTab); }

    @Override
    protected void initialize(ScreenArea screenArea, boolean firstOpen) {

        //+/- buttons for Early Payment limits
        this.addChild(PlainButton.builder()
                .position(screenArea.pos.offset(30,20))
                .sprite(IconAndButtonUtil.SPRITE_PLUS)
                .pressAction(() -> this.commonTab.SetMaxRentPayments(Math.min(99,this.getMaxRentPayments() + this.getDeltaValue())))
                .addon(EasyAddonHelper.activeCheck(() -> this.getMaxRentPayments() < 99))
                .build());
        this.addChild(PlainButton.builder()
                .position(screenArea.pos.offset(30,30))
                .sprite(IconAndButtonUtil.SPRITE_MINUS)
                .pressAction(() -> this.commonTab.SetMaxRentPayments(Math.max(1,this.getMaxRentPayments() - this.getDeltaValue())))
                .addon(EasyAddonHelper.activeCheck(() -> this.getMaxRentPayments() > 1))
                .build());

        //Rent Duration Input
        this.addChild(TimeInputWidget.builder()
                .position(screenArea.pos.offset((screenArea.width / 2) - 25,85))
                .startTime(this.getRentDuration())
                .unitRange(TimeUtil.TimeUnit.HOUR,TimeUtil.TimeUnit.DAY)
                .maxDuration(ClaimShopData.MAX_RENT_DURATION)
                .handler(duration -> this.commonTab.SetRentDuration(duration.miliseconds))
                .addon(EasyAddonHelper.activeCheck(() -> !this.commonTab.isLocked()))
                .build());

    }

    @Override
    public void renderBG(EasyGuiGraphics gui) {

        //Max Payments Label
        gui.drawString(FTBChunksText.GUI_CLAIM_SHOP_MAX_RENT_PAYMENTS.get(this.getMaxRentPayments()),42,26,0x404040);

        //Rent Duration Label
        long duration = this.getRentDuration();
        Component message;
        if(duration < ClaimShopData.MIN_RENT_DURATION)
            message = FTBChunksText.GUI_CLAIM_SHOP_RENT_DURATION_EMPTY.get();
        else
            message = FTBChunksText.GUI_CLAIM_SHOP_RENT_DURATION.get(new TimeUtil.TimeData(duration).getString());
        TextRenderUtil.drawCenteredMultilineText(gui,message,30,this.screen.getXSize() - 60, 55, 0x404040);

    }

    private int getMaxRentPayments()
    {
        if(this.menu.getTrader() instanceof ClaimShopData shop)
            return shop.getMaxRentPayments();
        return 1;
    }

    private int getDeltaValue() {
        int amount = 1;
        if(Screen.hasShiftDown())
            amount += 9;
        if(Screen.hasControlDown())
            amount *= 10;
        return amount;
    }

    private long getRentDuration()
    {
        if(this.menu.getTrader() instanceof ClaimShopData shop)
            return shop.getRentDuration();
        return 0;
    }

    @Override
    public IconData getIcon() { return IconUtil.ICON_TRADER_ALT; }
    @Nullable
    @Override
    public Component getTooltip() {
        return FTBChunksText.TOOLTIP_CLAIM_SHOP_RENT.get();
    }

}