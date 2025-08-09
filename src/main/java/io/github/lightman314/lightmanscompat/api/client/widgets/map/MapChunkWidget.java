package io.github.lightman314.lightmanscompat.api.client.widgets.map;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import io.github.lightman314.lightmanscompat.LCompat;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.client.gui.easy.interfaces.ITooltipWidget;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.util.VersionUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import org.joml.Matrix4f;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class MapChunkWidget extends EasyButton implements ITooltipWidget {

    public static final ResourceLocation WIDGETS = VersionUtil.modResource(LCompat.MODID,"textures/gui/ftbchunks/map_widgets.png");

    private final ChunkPos chunk;
    private final Function<ChunkPos,ChunkFrame> frame;
    private final Function<ChunkPos,List<Component>> tooltips;
    //private final IMapWidgetHandler handler;
    private final ChunkImage image;

    protected MapChunkWidget(Builder builder) {
        super(builder);
        this.chunk = builder.chunk;
        this.frame = builder.frame;
        this.tooltips = builder.tooltips;
        //this.handler = builder.handler;
        this.image = new ChunkImage(Minecraft.getInstance().level,this.chunk,builder.yLevel);
    }

    @Override
    protected void renderWidget(EasyGuiGraphics gui) {

        //Render the map Texture
        this.image.bindTexture();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f,1f,1f,1f);

        float x = this.getX() + 1;
        float y = this.getY() + 1;
        float width = 16;
        float height = 16;

        Matrix4f matrix = gui.getPose().last().pose();
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.addVertex(matrix,x,y + height,0).setUv(0,1);
        buffer.addVertex(matrix,x + width,y + height,0).setUv(1,1);
        buffer.addVertex(matrix,x + width,y,0).setUv(1,0);
        buffer.addVertex(matrix,x,y,0).setUv(0,0);

        BufferUploader.drawWithShader(buffer.buildOrThrow());

        //Render the frame and/or highlight
        ChunkFrame frame = this.frame.apply(this.chunk);
        int v = this.isHovered() && this.isActive() ? 18 : 0;
        gui.blit(WIDGETS,0,0,frame.u,v,18,18);

    }

    public static Builder builder() { return new Builder(); }

    @Override
    public List<Component> getTooltipText() { return this.tooltips.apply(this.chunk); }

    public void discard() { this.image.dispose(); }

    public static class Builder extends EasyButtonBuilder<Builder>
    {

        private Builder() { super(18,18); }

        @Override
        protected Builder getSelf() { return this; }

        private ChunkPos chunk = new ChunkPos(0,0);
        private int yLevel = 0;
        private Function<ChunkPos,ChunkFrame> frame = c -> ChunkFrame.NEUTRAL;
        private Function<ChunkPos,List<Component>> tooltips = c -> new ArrayList<>();
        private IMapWidgetHandler handler = (l,c) -> {};

        public Builder withChunk(ChunkPos chunk) { this.chunk = chunk; return this; }
        public Builder withYLevel(int yLevel) { this.yLevel = yLevel; return this; }
        public Builder withFrame(Function<ChunkPos,ChunkFrame> frame) { this.frame = frame; return this; }
        public Builder withTooltips(Function<ChunkPos,List<Component>> tooltips) { this.tooltips = tooltips; return this; }
        public Builder withHandler(IMapWidgetHandler handler) { this.handler = handler; return this; }

        public MapChunkWidget build() { return new MapChunkWidget(this.pressAction(() -> this.handler.handleMapSelection(true,this.chunk)).altPressAction(() -> this.handler.handleMapSelection(false,this.chunk))); }

    }

}
