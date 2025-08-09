package io.github.lightman314.lightmanscompat.network.packets;

import io.github.lightman314.lightmanscompat.network.LCompatPacketHandler;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import net.minecraft.network.FriendlyByteBuf;

import javax.annotation.Nonnull;

public abstract class ClientToServerPacket extends CustomPacket {
    public final void send() {
        LCompatPacketHandler.instance.sendToServer(this);
    }

    public static class Simple extends ClientToServerPacket {
        public void encode(@Nonnull FriendlyByteBuf buffer) { }
    }
}
