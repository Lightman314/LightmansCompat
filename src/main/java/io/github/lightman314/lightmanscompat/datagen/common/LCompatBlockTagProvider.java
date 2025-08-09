package io.github.lightman314.lightmanscompat.datagen.common;

import io.github.lightman314.lightmanscompat.LCompat;
import io.github.lightman314.lightmanscompat.ftbchunks.core.FTBChunksBlocks;
import io.github.lightman314.lightmanscurrency.LCTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class LCompatBlockTagProvider extends BlockTagsProvider {

    public LCompatBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, LCompat.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .addOptional(getKey(FTBChunksBlocks.CLAIM_SHOP));

        this.tag(LCTags.Blocks.SAFE_INTERACTABLE)
                .addOptional(getKey(FTBChunksBlocks.CLAIM_SHOP));

    }

    private static ResourceLocation getKey(Supplier<? extends Block> block)  { return BuiltInRegistries.BLOCK.getKey(block.get()); }

}
