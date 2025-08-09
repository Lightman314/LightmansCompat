package io.github.lightman314.lightmanscompat.ftbchunks.packets;

import io.github.lightman314.lightmanscompat.LCompat;
import io.github.lightman314.lightmanscompat.ftbchunks.util.FTBChunksHelper;
import io.github.lightman314.lightmanscurrency.network.packet.ClientToServerPacket;
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
import java.util.UUID;

public class CRequestClaimData extends ClientToServerPacket {

    public static final Type<CRequestClaimData> TYPE = new Type<>(VersionUtil.modResource(LCompat.MODID,"c_request_claim_data"));
    public static final Handler<CRequestClaimData> HANDLER = new H();

    public final ResourceKey<Level> level;
    public final ChunkPos pos;
    public CRequestClaimData(ResourceKey<Level> level, ChunkPos pos) {
        super(TYPE);
        this.level = level;
        this.pos = pos;
    }

    private static void encode(FriendlyByteBuf buffer,CRequestClaimData message)
    {
        buffer.writeUtf(message.level.location().toString());
        buffer.writeLong(message.pos.toLong());
    }

    private static CRequestClaimData decode(FriendlyByteBuf buffer)
    {
        return new CRequestClaimData(ResourceKey.create(Registries.DIMENSION,VersionUtil.parseResource(buffer.readUtf())),new ChunkPos(buffer.readLong()));
    }

    private static class H extends Handler<CRequestClaimData>
    {

        protected H() { super(TYPE, StreamCodec.of(CRequestClaimData::encode,CRequestClaimData::decode)); }

        @Override
        protected void handle(@Nonnull CRequestClaimData message, @Nonnull IPayloadContext context, @Nonnull Player player) {
            UUID teamId = FTBChunksHelper.getChunkOwnerID(message.level,message.pos,false);
            context.reply(new SClaimDataReply(message.level,message.pos,teamId));
        }

    }

}
