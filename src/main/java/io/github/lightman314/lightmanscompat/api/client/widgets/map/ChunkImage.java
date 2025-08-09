package io.github.lightman314.lightmanscompat.api.client.widgets.map;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.MapColor;

//Mostly copied from ChunkLoaders ChunkImage class
//https://github.com/SuperMartijn642/ChunkLoaders/blob/forge-1.21/src/main/java/com/supermartijn642/chunkloaders/screen/ChunkImage.java
public class ChunkImage {

    private final Level level;
    private final ChunkPos chunkPos;
    private final int yLevel;
    private DynamicTexture texture;
    public ChunkImage(Level level, ChunkPos chunk, int yLevel)
    {
        this.level = level;
        this.chunkPos = chunk;
        this.yLevel = yLevel;
    }

    public void bindTexture() {
        if(this.texture == null)
            this.texture = new DynamicTexture(this.createImage());
        RenderSystem.setShaderTexture(0,this.texture.getId());
    }

    public void dispose() {
        if(this.texture != null)
        {
            this.texture.close();
            this.texture = null;
        }
    }

    private NativeImage createImage() {
        int width = 16;
        int height = 16;

        NativeImage image = new NativeImage(NativeImage.Format.RGBA,16,16,false);

        int startX = this.chunkPos.getMinBlockX();
        int startZ = this.chunkPos.getMinBlockZ();
        for(int x = 0; x < width; x++)
        {
            for(int z = 0; z < height; ++z)
            {
                BlockPos pos;
                int northY, westY;
                if(this.shouldDrawAtSameLayer()){
                    pos = this.getFirstBlockGoingDown(startX + x, this.yLevel + 1, startZ + z);
                    northY = this.getFirstBlockGoingDown(startX + x, this.yLevel + 1, startZ + z - 1).getY();
                    westY = this.getFirstBlockGoingDown(startX + x - 1, this.yLevel + 1, startZ + z).getY();
                }else{
                    pos = this.level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, new BlockPos(startX + x, 0, startZ + z)).below();
                    northY = this.level.getHeight(Heightmap.Types.WORLD_SURFACE, pos.getX(), pos.getZ() - 1) - 1;
                    westY = this.level.getHeight(Heightmap.Types.WORLD_SURFACE, pos.getX() - 1, pos.getZ()) - 1;
                }
                BlockState state = this.level.getBlockState(pos);
                MapColor color = state.getMapColor(this.level,pos);
                //Null check for safety
                int rgb = color == null ? MapColor.NONE.col : color.col;

                int red = (rgb >> 16) & 255;
                int green = (rgb >> 8) & 255;
                int blue = rgb & 255;

                if((pos.getY() > northY && northY >= this.level.getMinBuildHeight()) || (pos.getY() > westY && westY >= this.level.getMinBuildHeight()))
                {
                    if(red == 0 && green == 0 && blue == 0)
                    {
                        red = 3;
                        green = 3;
                        blue = 3;
                    }
                    else {
                        if(red > 0 && red < 3) red = 3;
                        if(green > 0 && green < 3) green = 3;
                        if(blue > 0 && blue < 3) blue = 3;
                        red = Math.min((int)(red / 0.7),255);
                        green = Math.min((int)(green / 0.7),255);
                        blue = Math.min((int)(blue / 0.7),255);
                    }
                }
                if((pos.getY() < northY && northY >= this.level.getMinBuildHeight()) || (pos.getY() < westY && westY >= this.level.getMinBuildHeight()))
                {
                    red = Math.max((int)(red * 0.7), 0);
                    green = Math.max((int)(green * 0.7), 0);
                    blue = Math.max((int)(blue * 0.7), 0);
                }

                image.setPixelRGBA(x, z, (255 << 24) | (blue << 16) | (green << 8) | red);
            }
        }

        return image;
    }

    private BlockPos getFirstBlockGoingDown(int x, int y, int z)
    {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x,y,z);
        while(this.level.isEmptyBlock(pos) && pos.getY() >= level.getMinBuildHeight())
            pos.setY(pos.getY() - 1);
        return pos;
    }

    private boolean shouldDrawAtSameLayer() {
        return this.level.dimensionType().hasCeiling();
    }

}
