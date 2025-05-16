package io.github.lightman314.lightmanscompat.datagen;

import io.github.lightman314.lightmanscompat.LCompat;
import io.github.lightman314.lightmanscompat.datagen.client.language.EnglishProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = LCompat.MODID)
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
