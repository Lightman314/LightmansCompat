package io.github.lightman314.lightmanscompat.ftbchunks.packets;

import io.github.lightman314.lightmanscompat.ftbchunks.util.FTBChunksHelper;
import io.github.lightman314.lightmanscompat.network.packets.ClientToServerPacket;
import io.github.lightman314.lightmanscurrency.util.VersionUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

public class CRequestClaimData extends ClientToServerPacket {

    public static final Handler<CRequestClaimData> HANDLER = new H();

    public final ResourceKey<Level> level;
    public final ChunkPos pos;
    public CRequestClaimData(ResourceKey<Level> level, ChunkPos pos) {
        this.level = level;
        this.pos = pos;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeUtf(this.level.location().toString());
        buffer.writeLong(this.pos.toLong());
    }

    private static class H extends Handler<CRequestClaimData>
    {

        protected H() { super(); }

        @Nonnull
        @Override
        public CRequestClaimData decode(@Nonnull FriendlyByteBuf buffer) {
            return new CRequestClaimData(ResourceKey.create(Registries.DIMENSION,VersionUtil.parseResource(buffer.readUtf())),new ChunkPos(buffer.readLong()));
        }

        @Override
        protected void handle(@Nonnull CRequestClaimData message, @Nullable ServerPlayer player) {
            UUID teamId = FTBChunksHelper.getChunkOwnerID(message.level,message.pos,false);
            new SClaimDataReply(message.level,message.pos,teamId).sendTo(Objects.requireNonNull(player));
        }

    }

}