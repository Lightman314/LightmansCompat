package io.github.lightman314.lightmanscompat;

import io.github.lightman314.lightmanscompat.core.LCompatRegistries;
import io.github.lightman314.lightmanscompat.ftbchunks.FTBChunksNode;
import io.github.lightman314.lightmanscompat.waystones.WaystonesNode;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("lcompat")
public class LCompat {

    public static final String MODID = "lcompat";

    public static final Logger LOGGER = LogManager.getLogger();

    public LCompat(IEventBus bus, Dist side)
    {
        LCompatRegistries.init(bus);
        //Load the node config
        NodeConfig.INSTANCE.reload();
        //Initialize the waystones node
        if(ModList.get().isLoaded("waystones") && NodeConfig.INSTANCE.waystonesNode.get())
            WaystonesNode.setup(bus,side.isClient());
        //Initialize the FTB Chunks node
        if(ModList.get().isLoaded("ftbchunks") && NodeConfig.INSTANCE.ftbchunksNode.get())
            FTBChunksNode.setup(bus,side.isClient());
    }

}
