package io.github.lightman314.lightmanscompat.ftbchunks.util;

import dev.architectury.event.CompoundEventResult;
import dev.ftb.mods.ftbchunks.api.ClaimResult;
import dev.ftb.mods.ftbchunks.api.ClaimedChunk;
import dev.ftb.mods.ftbchunks.api.event.ClaimedChunkEvent;
import io.github.lightman314.lightmanscompat.ftbchunks.FTBChunksText;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.ClaimShopData;
import net.minecraft.commands.CommandSourceStack;

public class ClaimInteractionHandler {

    private static boolean paused = false;

    public static void initialize()
    {
        ClaimedChunkEvent.BEFORE_CLAIM.register(ClaimInteractionHandler::beforeChunkInteraction);
        ClaimedChunkEvent.BEFORE_UNCLAIM.register(ClaimInteractionHandler::beforeChunkInteraction);
    }

    public static void pauseInterference() { paused = true; }
    public static void unpauseInterference() { paused = false; }

    private static CompoundEventResult<ClaimResult> beforeChunkInteraction(CommandSourceStack stack, ClaimedChunk chunk)
    {
        if(paused)
            return CompoundEventResult.pass();
        if(ClaimShopData.isChunkLocked(chunk.getPos().dimension(),chunk.getPos().getChunkPos()))
            return CompoundEventResult.interruptDefault(ClaimResult.customProblem(FTBChunksText.MESSAGE_CHUNK_LOCKED.getKey()));
        return CompoundEventResult.pass();
    }

}