package io.github.lightman314.lightmanscompat.waystones.client;

import io.github.lightman314.lightmanscompat.waystones.requirements.MoneyRequirement;
import net.blay09.mods.waystones.api.client.WaystonesClientAPI;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public class WaystonesClientNode {


    public static void setup(IEventBus bus)
    {
        bus.addListener(WaystonesClientNode::onClientSetup);
    }

    public static void onClientSetup(FMLClientSetupEvent event)
    {
        WaystonesClientAPI.registerRequirementRenderer(MoneyRequirement.class,new LCWarpRequirementRenderer());
    }

}
