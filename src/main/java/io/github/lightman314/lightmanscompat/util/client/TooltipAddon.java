package io.github.lightman314.lightmanscompat.util.client;

import io.github.lightman314.lightmanscurrency.client.gui.easy.WidgetAddon;
import io.github.lightman314.lightmanscurrency.client.gui.easy.interfaces.ITooltipSource;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyWidget;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Supplier;

public class TooltipAddon extends WidgetAddon implements ITooltipSource {

    private final Supplier<List<Component>> tooltipSource;
    private TooltipAddon(Supplier<List<Component>> tooltipSource) { this.tooltipSource = tooltipSource; }

    public static WidgetAddon tooltips(Supplier<List<Component>> tooltipSource) { return new TooltipAddon(tooltipSource); }

    @Override
    public List<Component> getTooltipText(int x, int y) {
        EasyWidget w = this.getWidget();
        return w != null && w.getArea().isMouseInArea(x,y) ? this.tooltipSource.get() : null;
    }
}
