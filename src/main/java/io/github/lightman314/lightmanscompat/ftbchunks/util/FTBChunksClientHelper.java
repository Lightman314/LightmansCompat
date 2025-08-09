package io.github.lightman314.lightmanscompat.ftbchunks.util;

import dev.ftb.mods.ftblibrary.math.ChunkDimPos;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.Team;
import dev.ftb.mods.ftbteams.api.client.ClientTeamManager;
import io.github.lightman314.lightmanscompat.ftbchunks.packets.CRequestClaimData;
import io.github.lightman314.lightmanscompat.ftbchunks.packets.SClaimDataReply;
import io.github.lightman314.lightmanscurrency.api.misc.player.PlayerReference;
import io.github.lightman314.lightmanscurrency.util.TimeUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FTBChunksClientHelper {

    private static final Map<ChunkDimPos,ClaimData> teamCache = new HashMap<>();
    private static ClaimData getData(ResourceKey<Level> level, ChunkPos pos)
    {
        ChunkDimPos key = new ChunkDimPos(level,pos);
        if(!teamCache.containsKey(key))
            teamCache.put(key,new ClaimData());
        return teamCache.get(key);
    }

    public static boolean isChunkOwner(PlayerReference owner, ResourceKey<Level> level, ChunkPos pos)
    {
        UUID teamID = getChunkOwnerID(level,pos);
        if(teamID == null)
            return false;
        Team team = FTBTeamHelper.getPlayersTeam(owner,true);
        return team != null && team.getTeamId().equals(teamID);
    }

    @Nullable
    public static Component getChunkOwnerName(ResourceKey<Level> level, ChunkPos pos)
    {
        //Run on Client only
        UUID teamID = getChunkOwnerID(level,pos);
        if(teamID == null)
            return null;
        if(FTBTeamsAPI.api().isClientManagerLoaded())
        {
            ClientTeamManager manager = FTBTeamsAPI.api().getClientManager();
            if(manager == null)
                return null;
            Team team = manager.getTeamByID(teamID).orElse(null);
            return team == null ? null : team.getName();
        }
        return null;
    }

    @Nullable
    public static UUID getChunkOwnerID(ResourceKey<Level> level, ChunkPos pos)
    {
        ClaimData data = getData(level,pos);
        if(data.hasExpired())
        {
            new CRequestClaimData(level,pos).send();
            //FTBChunksNode.LOGGER.debug("Requesting owner info for {}: {}",level.location(),pos);
            data.updateRequestSent = true;
        }
        return data.teamID;
    }

    public static void handlePacket(SClaimDataReply packet)
    {
        ClaimData data = getData(packet.level,packet.chunk);
        data.update(packet.teamID);
        //FTBChunksNode.LOGGER.debug("Received owner info for {}: {}",packet.level.location(),packet.chunk);
    }

    private static class ClaimData
    {
        UUID teamID = null;
        long timestamp = 0;
        boolean updateRequestSent = false;
        void update(@Nullable UUID teamID)
        {
            this.teamID = teamID;
            this.timestamp = TimeUtil.getCurrentTime();
            this.updateRequestSent = false;
        }
        boolean hasExpired() { return !this.updateRequestSent && !TimeUtil.compareTime(5000,this.timestamp); }
    }

}