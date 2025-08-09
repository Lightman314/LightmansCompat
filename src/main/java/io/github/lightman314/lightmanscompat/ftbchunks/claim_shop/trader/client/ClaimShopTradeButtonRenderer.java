package io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.client;

import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.ClaimShopTrade;
import io.github.lightman314.lightmanscurrency.api.traders.TradeContext;
import io.github.lightman314.lightmanscurrency.api.traders.trade.client.TradeRenderManager;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.trade.AlertData;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.trade.DisplayData;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.trade.DisplayEntry;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import net.minecraftforge.common.util.LazyOptional;

import java.util.List;

public class ClaimShopTradeButtonRenderer extends TradeRenderManager<ClaimShopTrade> {

    public ClaimShopTradeButtonRenderer(ClaimShopTrade trade) { super(trade); }

    @Override
    public int tradeButtonWidth(TradeContext context) {
        return 100;
    }

    @Override
    public LazyOptional<ScreenPosition> arrowPosition(TradeContext context) {
        return LazyOptional.empty();
    }

    @Override
    public DisplayData inputDisplayArea(TradeContext context) {
        return new DisplayData(0,0,20,20);
    }

    @Override
    public List<DisplayEntry> getInputDisplays(TradeContext context) { return this.lazyPriceDisplayList(context); }

    @Override
    public DisplayData outputDisplayArea(TradeContext context) {
        return new DisplayData(20,0,20,20);
    }

    @Override
    public List<DisplayEntry> getOutputDisplays(TradeContext context) {
        return List.of();
    }

    @Override
    protected void getAdditionalAlertData(TradeContext context, List<AlertData> list) {

    }

}