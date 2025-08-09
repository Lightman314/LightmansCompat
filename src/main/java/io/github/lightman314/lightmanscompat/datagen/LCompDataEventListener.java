package io.github.lightman314.lightmanscompat.datagen;

import io.github.lightman314.lightmanscompat.LCompat;
import io.github.lightman314.lightmanscompat.datagen.client.LCompatBlockStateProvider;
import io.github.lightman314.lightmanscompat.datagen.client.language.EnglishProvider;
import io.github.lightman314.lightmanscompat.datagen.common.LCompatBlockTagProvider;
import io.github.lightman314.lightmanscompat.datagen.common.LCompatRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = LCompat.MODID)
public class LCompDataEventListener {

    @SubscribeEvent
    public static void onDataGen(GatherDataEvent event)
    {

        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupHolder = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        //Block Tags
        generator.addProvider(event.includeServer(),new LCompatBlockTagProvider(output,lookupHolder,existingFileHelper));

        //Recipes
        generator.addProvider(event.includeServer(),new LCompatRecipeProvider(output,lookupHolder));

        //Block States and models
        generator.addProvider(event.includeClient(),new LCompatBlockStateProvider(output,existingFileHelper));

        //Language
        generator.addProvider(event.includeClient(),new EnglishProvider(output));

    }

}
