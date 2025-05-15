package io.github.lightman314.lightmanscompat.datagen;

import io.github.lightman314.lightmanscompat.LCompat;
import io.github.lightman314.lightmanscompat.datagen.client.language.EnglishProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = LCompat.MODID)
public class LCompDataEventListener {

    @SubscribeEvent
    public static void onDataGen(GatherDataEvent event)
    {

        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        //CompletableFuture<HolderLookup.Provider> lookupHolder = event.getLookupProvider();
        //ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        //Language
        generator.addProvider(event.includeClient(),new EnglishProvider(output));

    }

}
