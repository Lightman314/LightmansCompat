package io.github.lightman314.lightmanscompat.datagen;

import io.github.lightman314.lightmanscompat.LCompat;
import io.github.lightman314.lightmanscompat.datagen.client.LCompatBlockStateProvider;
import io.github.lightman314.lightmanscompat.datagen.client.language.EnglishProvider;
import io.github.lightman314.lightmanscompat.datagen.common.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = LCompat.MODID)
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
        generator.addProvider(event.includeServer(),new LCompatRecipeProvider(output));

        //Block States and Models
        generator.addProvider(event.includeClient(),new LCompatBlockStateProvider(output,existingFileHelper));

        //Language
        generator.addProvider(event.includeClient(),new EnglishProvider(output));

    }

}
