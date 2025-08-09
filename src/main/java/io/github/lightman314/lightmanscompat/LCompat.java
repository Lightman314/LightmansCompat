package io.github.lightman314.lightmanscompat;

import io.github.lightman314.lightmanscompat.core.LCompatRegistries;
import io.github.lightman314.lightmanscompat.ftbchunks.FTBChunksNode;
import io.github.lightman314.lightmanscompat.network.LCompatPacketHandler;
import io.github.lightman314.lightmanscompat.waystones.WaystonesNode;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("lcompat")
public class LCompat {

    public static final String MODID = "lcompat";

    public static final Logger LOGGER = LogManager.getLogger();

    @SuppressWarnings("removal")
    public LCompat()
    {
        //Why is something flagged for removal in MC 1.21 in forge for 1.20?
        //Seriously guys, get your shit together 1.20 has been around for years
        //you shouldn't be strong-arming us to change our code to be compatible with
        //a Minecraft version I'm not developing for...
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        boolean isClient = FMLEnvironment.dist.isClient();

        LCompatRegistries.init(bus);
        //Load the node config
        NodeConfig.INSTANCE.reload();

        //Initialize the waystones node
        if(ModList.get().isLoaded("waystones") && NodeConfig.INSTANCE.waystonesNode.get())
            WaystonesNode.setup();
        //Initialize the FTB Chunks node
        if(ModList.get().isLoaded("ftbchunks") && NodeConfig.INSTANCE.ftbchunksNode.get())
            FTBChunksNode.setup(bus,isClient);

        bus.addListener(this::commonSetup);

    }

    public void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(LCompatPacketHandler::init);
    }


}
