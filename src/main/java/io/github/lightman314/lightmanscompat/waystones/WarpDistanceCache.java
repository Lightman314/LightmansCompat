package io.github.lightman314.lightmanscompat.waystones;

import io.github.lightman314.lightmanscurrency.util.VersionUtil;
import net.blay09.mods.waystones.api.IWaystone;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class WarpDistanceCache {

    public static final WarpDistanceCache EMPTY = new WarpDistanceCache(VersionUtil.vanillaResource("overworld"),0d);

    public boolean saveData = true;
    public final ResourceLocation originalDimension;
    public final double distance;
    public boolean isEmpty() { return this == EMPTY; }
    public WarpDistanceCache(Player player, IWaystone target)
    {
        this.originalDimension = player.level().dimension().location();
        this.distance = player.position().distanceTo(target.getPos().getCenter());
    }
    private WarpDistanceCache(ResourceLocation originalDimension, double distance) { this.originalDimension = originalDimension; this.distance = distance; this.saveData = false; }
}
