package io.github.lightman314.lightmanscompat.datagen.client;

import io.github.lightman314.lightmanscompat.LCompat;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.block.ClaimShopState;
import io.github.lightman314.lightmanscompat.ftbchunks.core.FTBChunksBlocks;
import io.github.lightman314.lightmanscurrency.api.misc.blocks.IRotatableBlock;
import io.github.lightman314.lightmanscurrency.common.blocks.variant.IVariantBlock;
import io.github.lightman314.lightmanscurrency.util.VersionUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class LCompatBlockStateProvider extends BlockStateProvider {

    public LCompatBlockStateProvider(PackOutput output, ExistingFileHelper fileHelper) {
        super(output, LCompat.MODID, fileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.registerClaimShop(FTBChunksBlocks.CLAIM_SHOP,"ftb_claim_shop/inactive","ftb_claim_shop/for_sale","ftb_claim_shop/for_rent","ftb_claim_shop/sold","ftb_claim_shop/rented");
    }

    private void registerClaimShop(Supplier<? extends Block> block, String... models) {
        if(models.length != ClaimShopState.values().length)
            throw new IllegalArgumentException("Expected " + ClaimShopState.values().length + " claim shop models, got " + models.length);
        List<ModelFile> modelFiles = new ArrayList<>();
        for (String model : models)
            modelFiles.add(this.lazyBlockModel(model, true));
        this.buildState(block, (state) -> ConfiguredModel.builder().modelFile(modelFiles.get(state.getValue(ClaimShopState.PROPERTY).ordinal())).rotationY(this.getRotationY(state)).build());
        this.registerBlockItemModel(block, modelFiles.getFirst());
    }

    private void registerRotatable(Supplier<? extends Block> block) {
        this.registerRotatable(block, this.lazyBlockID(block), true);
    }

    private void registerRotatable(Supplier<? extends Block> block, String modelID, boolean check) {
        ModelFile model = this.lazyBlockModel(modelID, check);
        this.buildState(block, (state) -> ConfiguredModel.builder().modelFile(model).rotationY(this.getRotationY(state)).build());
        this.registerBlockItemModel(block, model);
    }

    private int getRotationY(BlockState state) {
        Block var3 = state.getBlock();
        if (var3 instanceof IRotatableBlock rb) {
            return rb.getRotationY(state);
        } else {
            return 0;
        }
    }

    private void registerBlockItemModel(Supplier<? extends Block> block, ModelFile itemModel) {
        this.itemModels().getBuilder(BuiltInRegistries.ITEM.getKey(block.get().asItem()).toString()).parent(itemModel);
    }


    private String lazyBlockID(Supplier<? extends Block> block) {
        return BuiltInRegistries.BLOCK.getKey(block.get()).getPath();
    }

    private ResourceLocation lazyItemModelID(String modelID) {
        return VersionUtil.modResource(LCompat.MODID,modelID.startsWith("item/") ? modelID : "item/" + modelID);
    }

    private ResourceLocation lazyBlockModelID(String modelID) {
        return VersionUtil.modResource(LCompat.MODID,modelID.startsWith("block/") ? modelID : "block/" + modelID);
    }

    private ModelFile lazyBlockModel(String modelID, boolean check) {
        return check ? new ModelFile.ExistingModelFile(this.lazyBlockModelID(modelID), this.models().existingFileHelper) : new ModelFile.UncheckedModelFile(this.lazyBlockModelID(modelID));
    }

    protected final void buildState(Supplier<? extends Block> block, Function<BlockState, ConfiguredModel[]> mapper) {
        this.buildState(block.get(), mapper);
    }

    protected final void buildState(Block block, Function<BlockState, ConfiguredModel[]> mapper) {
        VariantBlockStateBuilder builder = this.getVariantBuilder(block);
        if (block instanceof IVariantBlock) {
            builder.forAllStatesExcept(mapper, IVariantBlock.VARIANT);
        } else {
            builder.forAllStates(mapper);
        }

    }

}
