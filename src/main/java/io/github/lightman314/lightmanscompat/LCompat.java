package io.github.lightman314.lightmanscompat;

import io.github.lightman314.lightmanscompat.waystones.WaystonesNode;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod("lcompat")
public class LCompat {

    public static final String MODID = "lcompat";

    @SuppressWarnings("removal")
    public LCompat()
    {

        //Why is something flagged for removal in MC 1.21 in forge for 1.20?
        //Seriously guys, get your shit together 1.20 has been around for years
        //you shouldn't be strong-arming us to change our code to be compatible with
        //a Minecraft version I'm not developing for...
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        boolean isClient = FMLEnvironment.dist.isClient();

        //Initialize the waystones node
        if(ModList.get().isLoaded("waystones"))
            WaystonesNode.setup();
    }

}
