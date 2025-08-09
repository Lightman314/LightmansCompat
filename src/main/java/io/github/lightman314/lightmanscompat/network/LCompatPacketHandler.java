package io.github.lightman314.lightmanscompat.network;

import io.github.lightman314.lightmanscompat.LCompat;
import io.github.lightman314.lightmanscurrency.network.packet.ClientToServerPacket;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import io.github.lightman314.lightmanscurrency.network.packet.ServerToClientPacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD,modid = LCompat.MODID)
public class LCompatPacketHandler {

    private static final List<Consumer<PacketRegistration>> packetNodes = new ArrayList<>();
    public static void registerNode(Consumer<PacketRegistration> node) { packetNodes.add(node); }

    @SubscribeEvent
    public static void onPayloadRegister(RegisterPayloadHandlersEvent event)
    {
        PacketRegistration r = new PacketRegistration(event.registrar("1"));
        for(var consumer : packetNodes)
            consumer.accept(r);
    }

    public record PacketRegistration(PayloadRegistrar registrar)
    {
        public <T extends ServerToClientPacket> void registerS2C(CustomPacket.AbstractHandler<T> handler) {
            registrar.playToClient(handler.type, handler.codec, handler);
        }

        public <T extends ClientToServerPacket> void registerC2S(CustomPacket.AbstractHandler<T> handler) {
            registrar.playToServer(handler.type, handler.codec, handler);
        }

        public <T extends ServerToClientPacket> void registerConfigS2C(CustomPacket.ConfigHandler<T> handler) {
            registrar.configurationToClient(handler.type, handler.configCodec, handler);
        }
    }

}
