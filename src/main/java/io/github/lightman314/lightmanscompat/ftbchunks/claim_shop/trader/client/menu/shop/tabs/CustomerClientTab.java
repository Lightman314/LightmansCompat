package io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.client.menu.shop.tabs;

import io.github.lightman314.lightmanscompat.ftbchunks.FTBChunksText;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.ClaimShopData;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.client.menu.shop.ClaimShopClientTab;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.menu.shop.tabs.CustomerTab;
import io.github.lightman314.lightmanscompat.api.client.widgets.map.MapRegionWidget;
import io.github.lightman314.lightmanscompat.ftbchunks.util.FTBTeamHelper;
import io.github.lightman314.lightmanscompat.util.client.TooltipAddon;
import io.github.lightman314.lightmanscurrency.api.misc.EasyText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.misc.player.PlayerReference;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.traders.TradeContext;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.TraderScreen;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.trade.AlertData;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyAddonHelper;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyTextButton;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import io.github.lightman314.lightmanscurrency.common.util.IconData;
import io.github.lightman314.lightmanscurrency.common.util.IconUtil;
import io.github.lightman314.lightmanscurrency.util.TimeUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.ChunkPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CustomerClientTab extends ClaimShopClientTab<CustomerTab> {

    private final ScreenPosition INFO_WIDGET_POSITION = ScreenPosition.of(175, 160);

    public CustomerClientTab(@Nonnull Object screen, @Nonnull CustomerTab commonTab) { super(screen, commonTab); }

    @Override
    protected void initialize(ScreenArea screenArea, boolean firstOpen) {

        ClaimShopData shop = this.menu.getTrader();
        if(shop != null)
        {
            MapRegionWidget w = this.addChild(MapRegionWidget.builder()
                    .position(screenArea.pos.offset(22,14))
                    .withRadius(ClaimShopData.CLAIM_RANGE)
                    .withCenterChunk(shop.getWorldPosition())
                    .withSimpleFrame(this::isChunkSelected)
                    .build());
            w.setActive(false);
        }

        this.addChild(EasyTextButton.builder()
                .position(screenArea.pos.offset(22,14 + (7 * 18)))
                .width(7 * 18)
                .text(this::tradeButtonText)
                .pressAction(this.commonTab::ExecuteTrade)
                .addon(TooltipAddon.tooltips(this::tradeButtonTooltip))
                .addon(EasyAddonHelper.activeCheck(this::allowTradeInteraction))
                .build());

    }

    @Override
    public void renderBG(@Nonnull EasyGuiGraphics gui) {

        //Render Money Info Widget
        gui.blit(TraderScreen.GUI_TEXTURE, this.INFO_WIDGET_POSITION, TraderScreen.WIDTH + 38, 0, 10, 10);

        //Render trader title
        ClaimShopData shop = this.menu.getTrader();
        gui.drawString(shop == null ? EasyText.empty() : shop.getTitle(),4,5,0x404040);

        //Renter Slot Backgrounds for the money slots
        for(Slot s : this.commonTab.getMoneySlots())
            gui.renderSlot(this.screen,s);

        //Render Available Funds
        Component valueText = this.commonTab.getContext(shop).getAvailableFunds().getRandomValueLine();
        gui.drawString(valueText, 185 - gui.font.width(valueText) - 10, this.screen.getYSize() - 94, 4210752);

    }

    @Override
    public void renderAfterWidgets(@Nonnull EasyGuiGraphics gui) {
        if (this.INFO_WIDGET_POSITION.offset(this.screen).isMouseInArea(gui.mousePos, 10, 10)) {
            gui.renderComponentTooltip(this.commonTab.getContext(this.menu.getTrader()).getAvailableFundsDescription());
        }
    }

    private boolean isChunkSelected(ChunkPos chunk)
    {
        ClaimShopData shop = this.menu.getTrader();
        return shop != null && shop.getChunkTargets().contains(chunk);
    }

    private Component tradeButtonText()
    {
        ClaimShopData shop = this.menu.getTrader();
        int chunkCount = shop == null ? 0 : shop.getChunkTargets().size();
        return shop != null && shop.isRentMode() ? FTBChunksText.BUTTON_CLAIM_SHOP_RENT.get(chunkCount) : FTBChunksText.BUTTON_CLAIM_SHOP_PURCHASE.get(chunkCount);
    }

    private List<Component> tradeButtonTooltip()
    {
        List<Component> tooltip = new ArrayList<>();
        ClaimShopData shop = this.menu.getTrader();
        if(shop == null)
            return tooltip;
        List<AlertData> alerts = shop.runPreTradeEvent(shop.getTrade(),this.commonTab.getContext(shop)).getAlertInfo();
        //Manually add custom alerts
        alerts.sort(AlertData::compare);
        //Manually add price first
        MoneyValue price = shop.getTrade().getCost(this.commonTab.getContext(shop));
        if(shop.isRentMode())
        {
            tooltip.add(FTBChunksText.TOOLTIP_CLAIM_SHOP_RENT_PRICE.get(price.getText(),new TimeUtil.TimeData(shop.getRentDuration()).getShortString()));
            //Rent Status info
            PlayerReference renter = shop.getCustomer();
            if(shop.isCurrentlyRented() && renter != null)
            {
                long timeRemaining = shop.rentTimeRemaining();
                if(timeRemaining > 0)
                {
                    Component teamName = FTBTeamHelper.getPlayersTeamName(renter,true);
                    if(teamName == null)
                        teamName = renter.getNameComponent(true);
                    tooltip.add(FTBChunksText.TOOLTIP_CLAIM_SHOP_RENT_STATUS.get (teamName,new TimeUtil.TimeData(timeRemaining).getShortString(2)));
                }
            }
        }
        else
            tooltip.add(FTBChunksText.TOOLTIP_CLAIM_SHOP_PURCHASE_PRICE.get(price.getText()));
        //Add Alerts
        for(AlertData alert : alerts)
            tooltip.add(alert.getFormattedMessage());
        return tooltip;
    }

    private boolean allowTradeInteraction()
    {
        ClaimShopData shop = this.menu.getTrader();
        if(shop == null)
            return false;
        TradeContext context = this.commonTab.getContext(shop);
        if(shop.runPreTradeEvent(shop.getTrade(),context).isCanceled())
            return false;
        MoneyValue price = shop.getTrade().getCost(context);
        return context.hasFunds(price);
    }

    @Nonnull
    @Override
    public IconData getIcon() { return IconData.of(IconUtil.ICON_TRADER); }

    @Nullable
    @Override
    public Component getTooltip() { return FTBChunksText.TOOLTIP_CLAIM_SHOP_CUSTOMER.get(); }

}