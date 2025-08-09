package io.github.lightman314.lightmanscompat.api.client.widgets.map;

import net.minecraft.world.level.ChunkPos;

public interface IMapWidgetHandler {
    void handleMapSelection(boolean selection, ChunkPos selectedChunks);
}