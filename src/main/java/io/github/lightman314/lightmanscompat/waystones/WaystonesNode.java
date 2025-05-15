package io.github.lightman314.lightmanscompat.waystones;

import io.github.lightman314.lightmanscompat.waystones.client.WaystonesClientNode;
import io.github.lightman314.lightmanscompat.waystones.requirements.MoneyRequirement;
import io.github.lightman314.lightmanscompat.waystones.requirements.functions.*;
import io.github.lightman314.lightmanscompat.waystones.requirements.parameters.*;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import net.blay09.mods.waystones.api.WaystonesAPI;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WaystonesNode {

    public static final String WAYSTONES_MODID = "waystones";
    public static final Logger LOGGER = LogManager.getLogger();

    public static void setup(IEventBus bus, boolean client)
    {
        //Register common setup event
        bus.addListener(WaystonesNode::commonSetup);
        //Setup the client side of this node
        if(client)
            WaystonesClientNode.setup(bus);
    }

    public static void commonSetup(FMLCommonSetupEvent event)
    {
        LightmansCurrency.safeEnqueueWork(event,"Error occurred setting up Waystones Compat Node",() -> {
            LOGGER.info("Registering Waystone Requirements, Parameters, and Modifiers");
            //Register Money Requirement
            WaystonesAPI.registerRequirementType(MoneyRequirement.TYPE);
            //Register Parameters
            WaystonesAPI.registerParameterSerializer(MoneyValueParameter.SERIALIZER);
            WaystonesAPI.registerParameterSerializer(ScaledMoneyValueParameter.SERIALIZER);
            //Register Requirement Modifiers
            WaystonesAPI.registerRequirementModifier(AddMoneyCost.INSTANCE);
            WaystonesAPI.registerRequirementModifier(ScaledAddMoneyCost.INSTANCE);
            WaystonesAPI.registerRequirementModifier(MultiplyMoneyCost.INSTANCE);
            WaystonesAPI.registerRequirementModifier(MinMoneyCost.INSTANCE);
            WaystonesAPI.registerRequirementModifier(MaxMoneyCost.INSTANCE);
        });
    }

}
