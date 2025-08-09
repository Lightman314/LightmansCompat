package io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.menu.shop.tabs;

import com.google.common.collect.ImmutableList;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.ClaimShopData;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.client.menu.shop.tabs.CustomerClientTab;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.menu.shop.ClaimShopMenu;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.menu.shop.ClaimShopTab;
import io.github.lightman314.lightmanscurrency.api.misc.menus.MoneySlot;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.traders.TradeContext;
import io.github.lightman314.lightmanscurrency.common.menus.slots.easy.EasySlot;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CustomerTab extends ClaimShopTab {

    public CustomerTab(@Nonnull ClaimShopMenu menu) { super(menu); }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Object createClientTab(Object screen) { return new CustomerClientTab(screen,this); }

    @Override
    public boolean canOpen(Player player) { return true; }

    private final Container money = new SimpleContainer(5);
    private final List<MoneySlot> moneySlots = new ArrayList<>();
    public List<MoneySlot> getMoneySlots() { return ImmutableList.copyOf(this.moneySlots); }

    @Override
    public void addStorageMenuSlots(Function<Slot, Slot> addSlot) {
        for(int x = 0; x < 5; ++x)
        {
            MoneySlot slot = new MoneySlot(this.money,x,167, 142 - (x * 18),this.menu.player);
            addSlot.apply(slot);
            slot.active = false;
            this.moneySlots.add(slot);
        }
    }

    public TradeContext getContext(@Nullable ClaimShopData shop) { return TradeContext.create(shop,this.menu.player).withCoinSlots(this.money).build(); }

    public void ExecuteTrade()
    {
        if(this.isClient())
            this.menu.SendMessage(this.builder().setFlag("ExecuteTrade"));
        else
        {
            ClaimShopData shop = this.menu.getTrader();
            if(shop == null)
                return;
            shop.TryExecuteTrade(this.getContext(shop),0);
        }
    }

    @Override
    public void onTabOpen() { EasySlot.SetActive(this.moneySlots); }

    @Override
    public void onTabClose() {
        this.menu.clearContainer(this.money);
        EasySlot.SetInactive(this.moneySlots);
    }

    @Override
    public void onMenuClose() { this.menu.clearContainer(this.money); }

    @Override
    public void receiveMessage(LazyPacketData message) {
        if(message.contains("ExecuteTrade"))
            this.ExecuteTrade();
    }

}