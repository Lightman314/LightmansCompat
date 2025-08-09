package io.github.lightman314.lightmanscompat.ftbchunks.packets;

import io.github.lightman314.lightmanscompat.LCompat;
import io.github.lightman314.lightmanscompat.ftbchunks.util.FTBChunksClientHelper;
import io.github.lightman314.lightmanscurrency.network.packet.ServerToClientPacket;
import io.github.lightman314.lightmanscurrency.util.VersionUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class SClaimDataReply extends ServerToClientPacket {

    public static final Type<SClaimDataReply> TYPE = new Type<>(VersionUtil.modResource(LCompat.MODID,"s_claim_data_reply"));
    public static final Handler<SClaimDataReply> HANDLER = new H();

    public final ResourceKey<Level> level;
    public final ChunkPos chunk;
    public UUID teamID;
    public SClaimDataReply(ResourceKey<Level> level, ChunkPos chunk, @Nullable UUID teamID) {
        super(TYPE);
        this.level = level;
        this.chunk = chunk;
        this.teamID = teamID;
    }

    private static void encode(FriendlyByteBuf buffer, SClaimDataReply message)
    {
        buffer.writeUtf(message.level.location().toString());
        buffer.writeLong(message.chunk.toLong());
        buffer.writeBoolean(message.teamID != null);
        if(message.teamID != null)
            buffer.writeUUID(message.teamID);
    }

    private static SClaimDataReply decode(FriendlyByteBuf buffer) {
        ResourceKey<Level> level = ResourceKey.create(Registries.DIMENSION, VersionUtil.parseResource(buffer.readUtf()));
        ChunkPos chunk = new ChunkPos(buffer.readLong());
        UUID teamID = null;
        if (buffer.readBoolean())
            teamID = buffer.readUUID();
        return new SClaimDataReply(level,chunk,teamID);
    }

    private static class H extends Handler<SClaimDataReply>
    {

        protected H() { super(TYPE, StreamCodec.of(SClaimDataReply::encode,SClaimDataReply::decode)); }

        @Override
        protected void handle(@Nonnull SClaimDataReply message, @Nonnull IPayloadContext context, @Nonnull Player player) { FTBChunksClientHelper.handlePacket(message); }

    }

}
