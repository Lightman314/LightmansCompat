package io.github.lightman314.lightmanscompat.waystones.client;

import io.github.lightman314.lightmanscompat.waystones.requirements.MoneyRequirement;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.trade.DisplayData;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.trade.DisplayEntry;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import net.blay09.mods.waystones.client.requirement.RequirementRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;

public class LCWarpRequirementRenderer implements RequirementRenderer<MoneyRequirement> {

    @Override
    public void renderWidget(Player player, MoneyRequirement requirement, GuiGraphics mcgui, int mouseX, int mouseY, float partialTicks, int x, int y) {
        EasyGuiGraphics gui = EasyGuiGraphics.create(mcgui,mouseX,mouseY,partialTicks);
        gui.pushOffset(ScreenPosition.of(x,y));

        MoneyValue price = requirement.price();
        if(price.isEmpty())
            return;
        //Render the price as well as we can with the space alotted
        DisplayEntry entry = price.getDisplayEntry(new ArrayList<>(),true);
        entry.render(gui,0,0, new DisplayData(0,0,32,16));
    }

    @Override
    public int getWidth(Player player, MoneyRequirement requirement) { return requirement.isEmpty() ? 0 : 32; }

}
