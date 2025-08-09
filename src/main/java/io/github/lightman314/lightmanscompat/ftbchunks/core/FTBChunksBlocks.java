package io.github.lightman314.lightmanscompat.ftbchunks.core;

import io.github.lightman314.lightmanscompat.core.LCompatRegistries;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.block.ClaimShopBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Supplier;

public class FTBChunksBlocks {

    public static void init() {}

    public static final Supplier<Block> CLAIM_SHOP;

    static {

        CLAIM_SHOP = register("ftb_claim_shop",() ->
            new ClaimShopBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.0F, Float.POSITIVE_INFINITY)
                    .sound(SoundType.METAL))
        );

    }

    private static Supplier<Block> register(String id, Supplier<Block> blockSupplier) {
        Supplier<Block> block = LCompatRegistries.BlockRegistry().register(id,blockSupplier);
        LCompatRegistries.ItemRegistry().register(id,() -> new BlockItem(block.get(),new Item.Properties()));
        return block;
    }

}
