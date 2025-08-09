package io.github.lightman314.lightmanscompat.api.client.widgets.map;

import com.google.common.collect.Lists;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.misc.world.WorldPosition;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyAddonHelper;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyWidgetWithChildren;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.ChunkPos;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class MapRegionWidget extends EasyWidgetWithChildren {

    private final int radius;
    private final ChunkPos centerChunk;
    private final int yLevel;
    private final Function<ChunkPos,ChunkFrame> frame;
    private final Function<ChunkPos, List<Component>> tooltips;
    private final IMapWidgetHandler handler;

    List<MapChunkWidget> chunkWidgets = new ArrayList<>();

    protected MapRegionWidget(Builder builder) {
        super(builder);
        this.radius = builder.radius;
        this.centerChunk = builder.centerChunk;
        this.yLevel = builder.yLevel;
        this.frame = builder.frame;
        this.tooltips = builder.tooltips;
        this.handler = builder.handler;
    }

    @Override
    public void addChildren(ScreenArea screenArea) {

        int size = 1 + (2 * this.radius);
        int topLeftX = this.centerChunk.x - this.radius;
        int topLeftZ = this.centerChunk.z - this.radius;
        for(int x = 0; x < size; ++x)
        {
            for(int z = 0; z < size; ++z)
            {
                MapChunkWidget w = this.addChild(MapChunkWidget.builder()
                        .position(screenArea.pos.offset(18 * x,18 * z))
                        .withChunk(new ChunkPos(topLeftX + x,topLeftZ + z))
                        .withYLevel(this.yLevel)
                        .withFrame(this.frame)
                        .withTooltips(this.tooltips)
                        .withHandler(this.handler)
                        .addon(EasyAddonHelper.visibleCheck(this::isVisible))
                        .addon(EasyAddonHelper.activeCheck(this::isActive))
                        .build());
                this.chunkWidgets.add(w);
            }
        }
    }

    @Override
    protected void renderWidget(EasyGuiGraphics gui) {
    }

    public static Builder builder() { return new Builder(); }

    public void discard()
    {
        for(MapChunkWidget w : this.chunkWidgets)
            w.discard();
    }

    public static class Builder extends EasyBuilder<Builder>
    {

        protected Builder() { this.withRadius(1); }
        @Override
        protected Builder getSelf() { return this; }

        private int radius = 1;
        private ChunkPos centerChunk = new ChunkPos(0,0);
        private int yLevel = 0;
        private Function<ChunkPos,ChunkFrame> frame = c -> ChunkFrame.NEUTRAL;
        private Function<ChunkPos, List<Component>> tooltips = c -> new ArrayList<>();
        private IMapWidgetHandler handler = (l, c) -> {};

        public Builder withRadius(int radius) {
            this.radius = Math.max(0,radius);
            int size = 18 + (radius * 36);
            this.changeSize(size,size);
            return this;
        }

        public Builder withCenterChunk(ChunkPos chunk) { this.centerChunk = chunk; return this; }
        public Builder withCenterChunk(BlockPos pos) { this.centerChunk = new ChunkPos(pos); return this.withYLevel(pos.getY()); }
        public Builder withCenterChunk(WorldPosition pos) { return this.withCenterChunk(pos.getPos()); }

        public Builder withYLevel(int yLevel) { this.yLevel = yLevel; return this; }

        public Builder withSimpleFrame(Predicate<ChunkPos> selected) { return this.withFrame(c -> selected.test(c) ? ChunkFrame.GOOD : ChunkFrame.NEUTRAL); }
        public Builder withFrame(Function<ChunkPos,ChunkFrame> frame) { this.frame = frame; return this; }
        public Builder withTooltip(Function<ChunkPos,Component> tooltip) {
            this.tooltips = c -> {
                Component t = tooltip.apply(c);
                return t == null ? new ArrayList<>() : Lists.newArrayList(t);
            };
            return this;
        }
        public Builder withTooltips(Function<ChunkPos,List<Component>> tooltips) { this.tooltips = tooltips; return this; }

        public Builder withHandler(Consumer<ChunkPos> handler) { this.handler = (i,c) -> handler.accept(c); return this; }
        public Builder withHandler(IMapWidgetHandler handler) { this.handler = handler; return this; }

        public MapRegionWidget build() { return new MapRegionWidget(this); }

    }

}
