package io.github.lightman314.lightmanscompat.ftbchunks.util;

import dev.ftb.mods.ftbchunks.api.*;
import dev.ftb.mods.ftblibrary.math.ChunkDimPos;
import dev.ftb.mods.ftbteams.api.Team;
import io.github.lightman314.lightmanscompat.ftbchunks.FTBChunksNode;
import io.github.lightman314.lightmanscurrency.api.misc.EasyText;
import io.github.lightman314.lightmanscurrency.api.misc.player.PlayerReference;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.server.ServerLifecycleHooks;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class FTBChunksHelper {

    /**
     * Unsure if the ClaimManager works on both the server and the client, requires testing
     */
    public static boolean isChunkOwner(PlayerReference owner, ResourceKey<Level> level, ChunkPos pos, boolean isClient)
    {
        if(isClient)
            return FTBChunksClientHelper.isChunkOwner(owner,level,pos);
        if(FTBChunksAPI.api().isManagerLoaded())
        {
            ClaimedChunkManager manager = FTBChunksAPI.api().getManager();
            ClaimedChunk chunk = manager.getChunk(new ChunkDimPos(level,pos));
            if(chunk == null)
                return false;
            ChunkTeamData teamData = chunk.getTeamData();
            return teamData != null && teamData.isTeamMember(owner.id);
        }
        else
            FTBChunksNode.LOGGER.warn("Attempted to see if player owns a chunk while the manager isn't loaded!");
        return false;
    }

    @Nullable
    public static Component getChunkOwnerName(ResourceKey<Level> level, ChunkPos pos, boolean isClient)
    {
        //Run on Client only
        if(isClient)
            return FTBChunksClientHelper.getChunkOwnerName(level,pos);
        if(FTBChunksAPI.api().isManagerLoaded())
        {
            ClaimedChunkManager manager = FTBChunksAPI.api().getManager();
            ClaimedChunk chunk = manager.getChunk(new ChunkDimPos(level,pos));
            if(chunk == null)
                return null;
            ChunkTeamData teamData = chunk.getTeamData();
            return teamData != null ? teamData.getTeam().getName() : null;
        }
        else
            FTBChunksNode.LOGGER.warn("Attempted to get chunk owners name when the manager was not loaded!");
        return null;
    }

    @Nullable
    public static UUID getChunkOwnerID(ResourceKey<Level> level, ChunkPos pos, boolean isClient)
    {
        if(isClient)
            return FTBChunksClientHelper.getChunkOwnerID(level,pos);
        else if(FTBChunksAPI.api().isManagerLoaded())
        {
            ClaimedChunkManager manager = FTBChunksAPI.api().getManager();
            if(manager == null)
                return null;
            ClaimedChunk chunk = manager.getChunk(new ChunkDimPos(level,pos));
            if(chunk == null)
                return null;
            ChunkTeamData teamData = chunk.getTeamData();
            if(teamData == null)
                return null;
            Team team = teamData.getTeam();
            return team == null ? null : team.getTeamId();
        }
        return null;
    }

    public static boolean canClaimChunks(PlayerReference owner, ResourceKey<Level> level, Iterable<ChunkPos> chunks, @Nullable PlayerReference oldOwner)
    {
        //Server only
        if(FTBChunksAPI.api().isManagerLoaded())
        {
            ClaimedChunkManager manager = FTBChunksAPI.api().getManager();
            int claimCount = 0;
            for(ChunkPos pos : chunks)
            {
                ClaimedChunk chunk = manager.getChunk(new ChunkDimPos(level,pos));
                if(chunk != null)
                {
                    ChunkTeamData teamData = chunk.getTeamData();
                    if(teamData != null)
                    {
                        //Chunk is already owned by the target
                        if(teamData.isTeamMember(owner.id))
                            continue;
                        //Not allowed if owned by someone other than the target team
                        if(oldOwner == null || !teamData.isTeamMember(oldOwner.id))
                            return false;
                    }
                }
                claimCount++;
            }
            //If claim count is greater than zero, confirm that the target team has enough spare claims to own the chunkns
            if(claimCount > 0)
            {
                Team team = FTBTeamHelper.getPlayersTeam(owner,false);
                if(team == null)
                    return false;
                //Server teams have infinite claims
                if(team.isServerTeam())
                    return true;
                ChunkTeamData teamData = manager.getOrCreateData(team);
                int spareClaims = teamData.getMaxClaimChunks() - teamData.getClaimedChunks().size();
                return spareClaims >= claimCount || team.isServerTeam();
            }
            else //Return false if the land is *already claimed* by the target owner
                return false;
        }
        return false;
    }

    public static boolean tryClaimChunk(PlayerReference owner, ResourceKey<Level> level, ChunkPos pos, @Nullable PlayerReference oldOwner)
    {
        //Server only
        if(FTBChunksAPI.api().isManagerLoaded())
        {
            ClaimedChunkManager manager = FTBChunksAPI.api().getManager();
            ClaimedChunk chunk = manager.getChunk(new ChunkDimPos(level,pos));
            if(chunk != null)
            {
                //Check if already owned by the owner
                ChunkTeamData teamData = chunk.getTeamData();
                if(teamData != null)
                {
                    if(oldOwner != null && teamData.isTeamMember(oldOwner.id))
                    {
                        ClaimInteractionHandler.pauseInterference();
                        ClaimResult result = teamData.unclaim(dummyCommandStack(),new ChunkDimPos(level,pos),false);
                        ClaimInteractionHandler.unpauseInterference();
                        if(result.isSuccess())
                            FTBChunksNode.LOGGER.debug("Unclaimed {} from {}",pos,oldOwner.getName(false));
                        else
                            FTBChunksNode.LOGGER.warn("Failed to unclaim {} from {} because {}",pos,oldOwner.getName(false),result.getMessage().getString());
                    }
                }
                if(teamData != null && teamData.isTeamMember(owner.id))
                {
                    FTBChunksNode.LOGGER.debug("Did not claim {} because it's already owned by {}",pos,owner.getName(false));
                    return true;
                }

            }
            //Chunk is not owned by the right team, or is not owned at all
            Team targetTeam = FTBTeamHelper.getPlayersTeam(owner,false);
            if(targetTeam == null)
            {
                FTBChunksNode.LOGGER.warn("Failed to claim {} as the intended owner ({}) doesn't have a team!",pos,owner.getName(false));
                return false;
            }
            ChunkTeamData teamData = manager.getOrCreateData(targetTeam);
            ClaimInteractionHandler.pauseInterference();
            ClaimResult result = teamData.claim(dummyCommandStack(),new ChunkDimPos(level,pos),false);
            ClaimInteractionHandler.unpauseInterference();
            if(!result.isSuccess())
                FTBChunksNode.LOGGER.warn("Failed to claim {} because {}",pos,result.getMessage().getString());
            return result.isSuccess();
        }
        return false;
    }

    private static CommandSourceStack dummyCommandStack()
    {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if(server == null)
            throw new RuntimeException("Cannot try to claim or unclaim chunks on the logical client!");
        return new CommandSourceStack(CommandSource.NULL,Vec3.ZERO,Vec2.ZERO,server.overworld(),2,"LCompat FTB Helper", EasyText.literal("LCompat FTB Helper"),server,null);
    }

}