package io.github.lightman314.lightmanscompat.datagen.common;

import com.google.common.collect.Lists;
import io.github.lightman314.lightmanscompat.LCompat;
import io.github.lightman314.lightmanscompat.ftbchunks.core.FTBChunksBlocks;
import io.github.lightman314.lightmanscurrency.common.core.ModBlocks;
import io.github.lightman314.lightmanscurrency.common.core.ModItems;
import io.github.lightman314.lightmanscurrency.util.VersionUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class LCompatRecipeProvider extends RecipeProvider {

    public LCompatRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> output) {

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FTBChunksBlocks.CLAIM_SHOP.get())
                .pattern("iei")
                .pattern("epe")
                .pattern("iei")
                .unlockedBy("paygate",LazyTrigger(ModBlocks.PAYGATE))
                .unlockedBy("trading_core",LazyTrigger(ModItems.TRADING_CORE))
                .unlockedBy("ender_pearl",LazyTrigger(Items.ENDER_PEARL))
                .define('i', Tags.Items.INGOTS_IRON)
                .define('e',Tags.Items.ENDER_PEARLS)
                .define('p',ModBlocks.PAYGATE.get())
                .save(lazyConditional(output,ItemID("ftbchunks/",FTBChunksBlocks.CLAIM_SHOP),new ModLoadedCondition("ftbchunks")),ItemID("ftbchunks/",FTBChunksBlocks.CLAIM_SHOP));

    }



    private static CriterionTriggerInstance LazyTrigger(ItemLike item) { return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(new ItemLike[]{item}).build()); }
    private static CriterionTriggerInstance LazyTrigger(Supplier<? extends ItemLike> item) { return LazyTrigger(item.get()); }
    private static CriterionTriggerInstance LazyTrigger(TagKey<Item> tag) { return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(tag).build()); }

    private static String ItemPath(ItemLike item) { return BuiltInRegistries.ITEM.getKey(item.asItem()).getPath(); }
    private static String ItemPath(Supplier<? extends ItemLike> item) { return ItemPath(item.get()); }
    private static ResourceLocation ItemID(String prefix, Supplier<? extends ItemLike> item) { return ID(prefix + ItemPath(item)); }
    private static ResourceLocation ItemID(String prefix, ItemLike item) { return ID(prefix + ItemPath(item)); }

    private static ResourceLocation ID(String path) { return VersionUtil.modResource(LCompat.MODID,path); }

    private static Consumer<FinishedRecipe> lazyConditional(Consumer<FinishedRecipe> consumer, ResourceLocation recipeID, ICondition... conditions) { return lazyConditional(consumer,recipeID,Lists.newArrayList(conditions)); }
    private static Consumer<FinishedRecipe> lazyConditional(Consumer<FinishedRecipe> consumer, ResourceLocation recipeID, List<ICondition> conditions)
    {
        return recipe -> {
            if(conditions.isEmpty())
            {
                consumer.accept(recipe);
                return;
            }
            ConditionalRecipe.Builder builder = ConditionalRecipe.builder();
            for(ICondition condition : conditions)
            {
                if(condition != null)
                    builder.addCondition(condition);
            }
            builder.addRecipe(recipe);
            builder.generateAdvancement(recipeID.withPrefix("recipes/misc/")).build(consumer,recipeID);
        };
    }

}