package io.github.lightman314.lightmanscompat.mixin.waystones;

import io.github.lightman314.lightmanscompat.waystones.WaystonesNode;
import io.github.lightman314.lightmanscompat.waystones.WaystonesText;
import io.github.lightman314.lightmanscurrency.api.capability.money.IMoneyHandler;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.money.MoneyAPI;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.trade.DisplayData;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.trade.DisplayEntry;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.client.gui.screen.WaystoneSelectionScreenBase;
import net.blay09.mods.waystones.client.gui.widget.WaystoneButton;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.core.WarpMode;
import net.blay09.mods.waystones.menu.WaystoneSelectionMenu;
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
        //Get the warp mode from the screen
        WarpMode mode = WarpMode.CUSTOM;
        if(Minecraft.getInstance().screen instanceof WaystoneSelectionScreenBase screen)
            mode = screen.getMenu().getWarpMode();
        else if(Minecraft.getInstance().player.containerMenu instanceof WaystoneSelectionMenu menu) //Backup method to obtain from the menu
            mode = menu.getWarpMode();
        else
            WaystonesNode.LOGGER.warn("Waystone button created from a non-waystone screen. May be unable to calculate the price accurately!");

        //Get the players leashed animals
        int leashed = PlayerWaystoneManager.findLeashedAnimals(Minecraft.getInstance().player).size();

        this.lightmansCompat$price = WaystonesNode.calculatePrice(player,this.waystone,mode,leashed);
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
        WaystoneButton self = this.lightmansCompat$self();
        EasyGuiGraphics gui = EasyGuiGraphics.create(mcgui,mouseX,mouseY,partialTicks);
        gui.pushOffset(self);
        DisplayEntry entry = this.lightmansCompat$price.getDisplayEntry(new ArrayList<>(),true);
        //Offset to avoid drawing over the XP cost if one is defined
        int xOffset = this.xpLevelCost > 0 ? 16 : 0;
        entry.render(gui,xOffset + 2,2,new DisplayData(0,0,32,16));

        if(self.isHovered() && mouseX >= self.getX() + xOffset && mouseX <= self.getX() + xOffset + 32)
        {
            gui.resetColor();
            gui.renderTooltip(WaystonesText.TOOLTIP_WAYSTONE_MONEY_COST.get(this.lightmansCompat$price.getText()));
        }
    }

}
