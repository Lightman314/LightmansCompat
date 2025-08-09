package io.github.lightman314.lightmanscompat.api.client.widgets.map;

public enum ChunkFrame {
    NEUTRAL(0),
    GOOD(18),
    WARNING(36),
    ERROR(54);

    public final int u;
    ChunkFrame(int u) { this.u = u; }

}
