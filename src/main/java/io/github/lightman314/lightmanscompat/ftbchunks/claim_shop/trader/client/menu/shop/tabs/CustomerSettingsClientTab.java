package io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.client.menu.shop.tabs;

import io.github.lightman314.lightmanscompat.ftbchunks.FTBChunksText;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.ClaimShopData;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.client.menu.shop.ClaimShopClientTab;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.menu.shop.tabs.CustomerSettingsTab;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.misc.player.PlayerReference;
import io.github.lightman314.lightmanscurrency.client.gui.widget.TimeInputWidget;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyAddonHelper;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyTextButton;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.client.util.TextRenderUtil;
import io.github.lightman314.lightmanscurrency.common.util.IconData;
import io.github.lightman314.lightmanscurrency.util.TimeUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CustomerSettingsClientTab extends ClaimShopClientTab<CustomerSettingsTab> {

    public CustomerSettingsClientTab(Object screen, CustomerSettingsTab commonTab) { super(screen, commonTab); }

    @Override
    protected void initialize(ScreenArea screenArea, boolean firstOpen) {

        ClaimShopData shop = this.menu.getTrader();

        this.addChild(TimeInputWidget.builder()
                .position(screenArea.pos.offset((screenArea.width / 2) - 40,40))
                .range(0,ClaimShopData.MAX_RENT_DURATION)
                .unitRange(TimeUtil.TimeUnit.MINUTE, TimeUtil.TimeUnit.DAY)
                .startTime(shop == null ? 0 : shop.getRentDueWarning())
                .handler(t -> this.commonTab.setAlertTime(t.miliseconds))
                .build());


        this.addChild(EasyTextButton.builder()
                .position(screenArea.pos.offset(30,110))
                .width(screenArea.width - 60)
                .text(FTBChunksText.BUTTON_CLAIM_SHOP_RENT_WARNING_CLEAR)
                .addon(EasyAddonHelper.tooltip(FTBChunksText.TOOLTIP_CLAIM_SHOP_RENT_WARNING_CLEAR))
                .addon(EasyAddonHelper.activeCheck(this::hasWarningBeenGiven))
                .pressAction(this.commonTab::clearAlertCache)
                .build());

        this.addChild(EasyTextButton.builder()
                .position(screenArea.pos.offset(30, 140))
                .width(screenArea.width - 60)
                .text(FTBChunksText.BUTTON_CLAIM_SHOP_END_RENT)
                .addon(EasyAddonHelper.tooltip(FTBChunksText.TOOLTIP_CLAIM_SHOP_END_RENT))
                .addon(EasyAddonHelper.activeCheck(this::canEndRental))
                .pressAction(this.commonTab::EndRentalEarly)
                .build());

    }

    @Override
    public void renderBG(EasyGuiGraphics gui) {

        ClaimShopData shop = this.menu.getTrader();
        if(shop == null)
            return;
        //Render info text
        PlayerReference customer = shop.getCustomer();
        long timeRemaining = shop.rentTimeRemaining();
        Component info;
        if(customer != null && timeRemaining > 0)
            info = FTBChunksText.TOOLTIP_CLAIM_SHOP_RENT_STATUS.get(customer.getName(true),new TimeUtil.TimeData(timeRemaining).getString(2));
        else
            info = FTBChunksText.GUI_CLAIM_SHOP_RENT_STATUS_NOT_RENTED.get();
        TextRenderUtil.drawCenteredMultilineText(gui,info,30,this.screen.getXSize() - 60,10,0x404040);

        //Render Warning
        Component notificationInfo;
        if(shop.getRentDueWarning() > 0)
            notificationInfo = FTBChunksText.GUI_CLAIM_SHOP_RENT_WARNING.get(new TimeUtil.TimeData(shop.getRentDueWarning()).getString());
        else
            notificationInfo = FTBChunksText.GUI_CLAIM_SHOP_RENT_WARNING_NONE.get();
        TextRenderUtil.drawCenteredMultilineText(gui,notificationInfo,30,this.screen.getXSize() - 60, 80, 0x404040);


    }

    private boolean hasWarningBeenGiven()
    {
        ClaimShopData shop = this.menu.getTrader();
        return shop != null && shop.getRentWarningGiven();
    }

    private boolean canEndRental()
    {
        ClaimShopData shop = this.menu.getTrader();
        return shop != null && shop.canEndRental(this.menu.player);
    }

    @Override
    public IconData getIcon() { return IconData.of(Items.PAPER); }
    
    @Override
    public Component getTooltip() { return FTBChunksText.TOOLTIP_CLAIM_SHOP_CUSTOMER_SETTINGS.get(); }

}
