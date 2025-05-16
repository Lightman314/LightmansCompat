package io.github.lightman314.lightmanscompat.mixin.waystones;

import io.github.lightman314.lightmanscompat.waystones.WaystonesNode;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.capability.money.IMoneyHandler;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.money.MoneyAPI;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.trade.DisplayData;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.trade.DisplayEntry;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.client.gui.widget.WaystoneButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(WaystoneButton.class)
public class WaystoneButtonMixin {

    @Unique
    private MoneyValue lightmansCompat$price;

    @Unique
    protected WaystoneButton lightmansCompat$self() { return (WaystoneButton)(Object)this; }
    @Final
    @Shadow
    private IWaystone waystone;
    @Shadow @Final
    private int xpLevelCost;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void init(int x, int y, IWaystone waystone, int xpLevelCost, Button.OnPress pressable, CallbackInfo ci) {
        WaystoneButton self = this.lightmansCompat$self();
        Player player = Minecraft.getInstance().player;
        if(player == null)
        {
            this.lightmansCompat$price = MoneyValue.empty();
            return;
        }
        this.lightmansCompat$price = WaystonesNode.calculatePrice(player,this.waystone);
        //Don't bother checking the players money if already inactive, player is creative, or if the price is empty
        if(this.lightmansCompat$price.isEmpty() || player.getAbilities().instabuild || !this.lightmansCompat$self().active)
            return;
        IMoneyHandler moneyHandler = MoneyAPI.API.GetPlayersMoneyHandler(player);
        //Set inactive if we cannot afford the warp
        if(!moneyHandler.getStoredMoney().containsValue(this.lightmansCompat$price))
            self.active = false;
    }

    @Inject(at = @At("RETURN"),method = "renderWidget")
    private void renderWidget(GuiGraphics mcgui, int mouseX, int mouseY, float partialTicks, CallbackInfo ci)
    {
        Player player = Minecraft.getInstance().player;
        if(player == null)
            return;
        if(this.lightmansCompat$price.isEmpty())
            return;
        //Render money price
        EasyGuiGraphics gui = EasyGuiGraphics.create(mcgui,mouseX,mouseY,partialTicks);
        gui.pushOffset(this.lightmansCompat$self());
        DisplayEntry entry = this.lightmansCompat$price.getDisplayEntry(new ArrayList<>(),true);
        //Offset to avoid drawing over the XP cost if one is defined
        int xOffset = this.xpLevelCost > 0 ? 16 : 0;
        entry.render(gui,xOffset + 2,2,new DisplayData(0,0,32,16));
    }

}
