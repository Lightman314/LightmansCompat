package io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader;

import io.github.lightman314.lightmanscompat.ftbchunks.FTBChunksText;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.client.ClaimShopTradeButtonRenderer;
import io.github.lightman314.lightmanscompat.ftbchunks.util.FTBTeamHelper;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.events.TradeEvent;
import io.github.lightman314.lightmanscurrency.api.misc.player.PlayerReference;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.traders.TradeContext;
import io.github.lightman314.lightmanscurrency.api.traders.trade.TradeData;
import io.github.lightman314.lightmanscurrency.api.traders.trade.TradeDirection;
import io.github.lightman314.lightmanscurrency.api.traders.trade.client.TradeInteractionData;
import io.github.lightman314.lightmanscurrency.api.traders.trade.client.TradeRenderManager;
import io.github.lightman314.lightmanscurrency.api.traders.trade.comparison.TradeComparisonResult;
import io.github.lightman314.lightmanscurrency.common.menus.traderstorage.core.BasicTradeEditTab;
import io.github.lightman314.lightmanscurrency.common.traders.rules.TradeRule;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ClaimShopTrade extends TradeData {

    public final ClaimShopData trader;
    public ClaimShopTrade(ClaimShopData trader) { super(false); this.trader = trader;}

    @Nonnull
    @Override
    public MoneyValue getCost() { return this.trader.getPrice(); }

    @Override
    public void beforeTrade(TradeEvent.PreTradeEvent event) {
        super.beforeTrade(event);
        //Manually inject custom warnings to the pre-trade event
        if(!this.trader.isActive())
        {
            event.addDenial(FTBChunksText.TOOLTIP_CLAIM_SHOP_NOT_ACTIVATED.get());
            //No need for any other denials if it's not activated
            return;
        }
        TradeContext context = event.getContext();
        MoneyValue cost = this.getCost(context);
        if(!context.hasFunds(cost))
            event.addWarning(LCText.TOOLTIP_CANNOT_AFFORD.get());
        //Same team warning
        if(FTBTeamHelper.isOnSameTeam(context.getPlayerReference(),this.trader.getOwner().getValidOwner().asPlayerReference(),this.trader.isClient()))
            event.addDenial(FTBChunksText.TOOLTIP_CLAIM_SHOP_SAME_TEAM.get());
        //Rent Alerts
        if(this.trader.isRentMode())
        {
            PlayerReference renter = this.trader.getCustomer();
            if(this.trader.isCurrentlyRented() && renter != null)
            {
                if(!FTBTeamHelper.isOnSameTeam(context.getPlayerReference(),renter,this.trader.isClient()))
                    event.addDenial(FTBChunksText.TOOLTIP_CLAIM_SHOP_RENTED_BY_OTHER.get());
                else if(this.trader.getPrepaidCount() >= this.trader.getMaxRentPayments())
                    event.addDenial(FTBChunksText.TOOLTIP_CLAIM_SHOP_RENT_LIMIT.get(this.trader.getMaxRentPayments()));
            }
        }
        else if(this.trader.hasBeenPurchased()) //Already Purchased Alert
            event.addDenial(FTBChunksText.TOOLTIP_CLAIM_SHOP_PURCHASED_BY_OTHER.get());
        //Group Limit
        if(this.trader.exceedsGroupLimit(context.getPlayerReference()))
            event.addDenial(FTBChunksText.TOOLTIP_CLAIM_SHOP_GROUP_LIMIT.get(this.trader.getGroupLimit()));
    }

    @Override
    public boolean allowTradeRule(TradeRule rule) { return false; }
    @Override
    public TradeDirection getTradeDirection() { return TradeDirection.SALE; }
    @Override
    public int getStock(@Nonnull TradeContext tradeContext) { return this.trader.getTradeStock(0); }
    @Override
    public TradeComparisonResult compare(TradeData tradeData) { return new TradeComparisonResult(); }
    @Override
    public boolean AcceptableDifferences(TradeComparisonResult tradeComparisonResult) { return false; }
    @Override
    public List<Component> GetDifferenceWarnings(TradeComparisonResult tradeComparisonResult) { return List.of(); }
    @Nonnull
    @Override
    @OnlyIn(Dist.CLIENT)
    public TradeRenderManager<?> getButtonRenderer() { return new ClaimShopTradeButtonRenderer(this); }
    @Override
    public void OnInputDisplayInteraction(@Nonnull BasicTradeEditTab basicTradeEditTab, int i, @Nonnull TradeInteractionData tradeInteractionData, @Nonnull ItemStack itemStack) { }
    @Override
    public void OnOutputDisplayInteraction(@Nonnull BasicTradeEditTab basicTradeEditTab, int i, @Nonnull TradeInteractionData tradeInteractionData, @Nonnull ItemStack itemStack) { }
    @Override
    public void OnInteraction(@Nonnull BasicTradeEditTab basicTradeEditTab, @Nonnull TradeInteractionData tradeInteractionData, @Nonnull ItemStack itemStack) { }

}