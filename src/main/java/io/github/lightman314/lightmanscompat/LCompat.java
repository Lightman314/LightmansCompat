package io.github.lightman314.lightmanscompat;

import io.github.lightman314.lightmanscompat.waystones.WaystonesNode;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;

@Mod("lcompat")
public class LCompat {

    public static final String MODID = "lcompat";

    public LCompat(IEventBus bus, Dist side)
    {
        //Initialize the waystones node
        if(ModList.get().isLoaded("waystones"))
            WaystonesNode.setup(bus,side.isClient());
    }

}
