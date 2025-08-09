package io.github.lightman314.lightmanscompat.ftbchunks.core;

import io.github.lightman314.lightmanscompat.core.LCompatRegistries;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.block.ClaimShopBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class FTBChunksBlockEntities {

    public static void init() {}

    public static final Supplier<BlockEntityType<ClaimShopBlockEntity>> CLAIM_SHOP;

    static {
        CLAIM_SHOP = register("ftb_claim_shop",ClaimShopBlockEntity::new,() -> new Block[]{FTBChunksBlocks.CLAIM_SHOP.get()});
    }

    private static <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String id, BlockEntityType.BlockEntitySupplier<T> builder, Supplier<Block[]> blocks)
    {
        return LCompatRegistries.BlockEntityRegistry().register(id,() -> BlockEntityType.Builder.of(builder,blocks.get()).build(null));
    }

}
