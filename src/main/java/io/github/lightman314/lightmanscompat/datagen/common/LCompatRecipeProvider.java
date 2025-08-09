package io.github.lightman314.lightmanscompat.datagen.common;

import io.github.lightman314.lightmanscompat.LCompat;
import io.github.lightman314.lightmanscompat.ftbchunks.core.FTBChunksBlocks;
import io.github.lightman314.lightmanscurrency.common.core.ModBlocks;
import io.github.lightman314.lightmanscurrency.common.core.ModItems;
import io.github.lightman314.lightmanscurrency.util.VersionUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class LCompatRecipeProvider extends RecipeProvider {

    public LCompatRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FTBChunksBlocks.CLAIM_SHOP.get())
                .pattern("iei")
                .pattern("epe")
                .pattern("iei")
                .unlockedBy("paygate",LazyTrigger(ModBlocks.PAYGATE))
                .unlockedBy("trading_core",LazyTrigger(ModItems.TRADING_CORE))
                .unlockedBy("ender_pearl",LazyTrigger(Items.ENDER_PEARL))
                .define('i',Tags.Items.INGOTS_IRON)
                .define('e',Tags.Items.ENDER_PEARLS)
                .define('p',ModBlocks.PAYGATE.get())
                .save(output.withConditions(new ModLoadedCondition("ftbchunks")),ItemID("ftbchunks/",FTBChunksBlocks.CLAIM_SHOP));

    }

    private static Criterion<?> LazyTrigger(ItemLike item) { return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(new ItemLike[]{item}).build()); }
    private static Criterion<?> LazyTrigger(Supplier<? extends ItemLike> item) { return LazyTrigger(item.get()); }
    private static Criterion<?> LazyTrigger(TagKey<Item> tag) { return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(tag).build()); }

    private static String ItemPath(ItemLike item) { return BuiltInRegistries.ITEM.getKey(item.asItem()).getPath(); }
    private static String ItemPath(Supplier<? extends ItemLike> item) { return ItemPath(item.get()); }
    private static ResourceLocation ItemID(String prefix, Supplier<? extends ItemLike> item) { return ID(prefix + ItemPath(item)); }
    private static ResourceLocation ItemID(String prefix, ItemLike item) { return ID(prefix + ItemPath(item)); }

    private static ResourceLocation ID(String path) { return VersionUtil.modResource(LCompat.MODID,path); }

}
