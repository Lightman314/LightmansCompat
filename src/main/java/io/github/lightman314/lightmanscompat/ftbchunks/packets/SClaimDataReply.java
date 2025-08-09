package io.github.lightman314.lightmanscompat.ftbchunks.packets;

import io.github.lightman314.lightmanscompat.ftbchunks.util.FTBChunksClientHelper;
import io.github.lightman314.lightmanscompat.network.packets.ServerToClientPacket;
import io.github.lightman314.lightmanscurrency.util.VersionUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class SClaimDataReply extends ServerToClientPacket {

    public static final Handler<SClaimDataReply> HANDLER = new H();

    public final ResourceKey<Level> level;
    public final ChunkPos chunk;
    public UUID teamID;
    public SClaimDataReply(ResourceKey<Level> level, ChunkPos chunk, @Nullable UUID teamID) {
        this.level = level;
        this.chunk = chunk;
        this.teamID = teamID;
    }

    @Override
    public void encode(FriendlyByteBuf buffer)
    {
        buffer.writeUtf(this.level.location().toString());
        buffer.writeLong(this.chunk.toLong());
        buffer.writeBoolean(this.teamID != null);
        if(this.teamID != null)
            buffer.writeUUID(this.teamID);
    }

    private static class H extends Handler<SClaimDataReply>
    {

        protected H() {  }

        @Nonnull
        @Override
        public SClaimDataReply decode(@Nonnull FriendlyByteBuf buffer) {
            ResourceKey<Level> level = ResourceKey.create(Registries.DIMENSION, VersionUtil.parseResource(buffer.readUtf()));
            ChunkPos chunk = new ChunkPos(buffer.readLong());
            UUID teamID = null;
            if (buffer.readBoolean())
                teamID = buffer.readUUID();
            return new SClaimDataReply(level,chunk,teamID);
        }

        @Override
        protected void handle(@Nonnull SClaimDataReply message, @Nullable ServerPlayer player) {
            FTBChunksClientHelper.handlePacket(message);
        }

    }

}