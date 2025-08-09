package io.github.lightman314.lightmanscompat.ftbchunks;

import io.github.lightman314.lightmanscompat.api.claimshop.ClaimGroupData;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.notifications.*;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.ClaimShopData;
import io.github.lightman314.lightmanscompat.ftbchunks.client.FTBChunksClientEvents;
import io.github.lightman314.lightmanscompat.ftbchunks.core.*;
import io.github.lightman314.lightmanscompat.ftbchunks.client.FTBChunksClientModEvents;
import io.github.lightman314.lightmanscompat.ftbchunks.packets.*;
import io.github.lightman314.lightmanscompat.ftbchunks.util.ClaimInteractionHandler;
import io.github.lightman314.lightmanscompat.network.LCompatPacketHandler;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.ModCreativeGroups;
import io.github.lightman314.lightmanscurrency.api.notifications.NotificationAPI;
import io.github.lightman314.lightmanscurrency.api.traders.TraderAPI;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FTBChunksNode {

    public static final Logger LOGGER = LogManager.getLogger();

    public static void setup(IEventBus bus, boolean isClient)
    {
        //Setup the blocks/items for registration
        FTBChunksBlocks.init();
        FTBChunksBlockEntities.init();
        FTBChunksMenus.init();

        bus.addListener(FTBChunksNode::registerCreativeTabs);
        bus.addListener(FTBChunksNode::commonSetup);

        ClaimInteractionHandler.initialize();

        //Initialize the common ClaimGroupData class
        ClaimGroupData.init();

        if(isClient)
        {
            bus.register(FTBChunksClientModEvents.class);
            MinecraftForge.EVENT_BUS.register(FTBChunksClientEvents.class);
        }

        //Register Packets
        LCompatPacketHandler.registerNode(FTBChunksNode::registerPackets);

    }

    private static void registerCreativeTabs(BuildCreativeModeTabContentsEvent event)
    {
        //Add claim shop to the trader group
        if(event.getTab() == ModCreativeGroups.TRADER_GROUP.get())
            event.accept(FTBChunksBlocks.CLAIM_SHOP.get());
    }

    private static void commonSetup(FMLCommonSetupEvent event)
    {
        LightmansCurrency.safeEnqueueWork(event,"Error during FTBChunks Compat common setup",() -> {

            //Register Traders
            TraderAPI.API.RegisterTrader(ClaimShopData.TYPE);

            //Register Notifications
            NotificationAPI.API.RegisterNotification(LandPurchaseNotification.TYPE);
            NotificationAPI.API.RegisterNotification(RentPaymentNotification.TYPE);
            NotificationAPI.API.RegisterNotification(RentDueNotification.TYPE);
            NotificationAPI.API.RegisterNotification(RentExpiredNotification.TYPE);

        });
    }

    private static void registerPackets(LCompatPacketHandler.PacketRegistration registration)
    {
        registration.register(CRequestClaimData.class,CRequestClaimData.HANDLER);
        registration.register(SClaimDataReply.class,SClaimDataReply.HANDLER);
    }

}