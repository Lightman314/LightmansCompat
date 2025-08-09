package io.github.lightman314.lightmanscompat.network;

import io.github.lightman314.lightmanscompat.LCompat;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import io.github.lightman314.lightmanscurrency.util.VersionUtil;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LCompatPacketHandler {

    public static final String PROTOCOL_VERSION = "1";

    public static SimpleChannel instance;

    private static final List<Consumer<PacketRegistration>> packetNodes = new ArrayList<>();
    public static void registerNode(Consumer<PacketRegistration> node) { packetNodes.add(node); }

    public static void init()
    {
        instance = NetworkRegistry.ChannelBuilder
                .named(VersionUtil.modResource(LCompat.MODID,"network"))
                .networkProtocolVersion(() -> PROTOCOL_VERSION)
                .clientAcceptedVersions(PROTOCOL_VERSION::equals)
                .serverAcceptedVersions(PROTOCOL_VERSION::equals)
                .simpleChannel();
        PacketRegistration r = new PacketRegistration(instance,0);
        for(var consumer : packetNodes)
            consumer.accept(r);
    }

    public static class PacketRegistration
    {

        private final SimpleChannel channel;
        private int nextID;
        private PacketRegistration(SimpleChannel channel,int nextID)
        {
            this.channel = channel;
            this.nextID = nextID;
        }

        public <T extends CustomPacket> void register(Class<T> clazz, CustomPacket.Handler<T> handler)
        {
            this.channel.registerMessage(nextID++,clazz,CustomPacket::encode,handler::decode,handler::handlePacket);
        }

    }

}