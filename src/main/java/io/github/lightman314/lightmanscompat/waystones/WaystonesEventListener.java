package io.github.lightman314.lightmanscompat.waystones;

import io.github.lightman314.lightmanscurrency.api.capability.money.IMoneyHandler;
import io.github.lightman314.lightmanscurrency.api.money.MoneyAPI;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.api.WaystoneTeleportEvent;
import net.blay09.mods.waystones.core.WarpMode;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;

public final class WaystonesEventListener {

    private WaystonesEventListener() {}

    private static final Map<UUID,WarpDistanceCache> warpDistanceCache = new HashMap<>();

    //Cancel the event if the player cannot afford the warp
    @SubscribeEvent
    public static void beforeTeleport(WaystoneTeleportEvent.Pre event)
    {
        Entity entity = event.getContext().getEntity();
        if(entity instanceof Player player && !player.getAbilities().instabuild)
        {
            IWaystone target = event.getContext().getTargetWaystone();
            WarpMode mode = event.getContext().getWarpMode();
            int leashed = event.getContext().getLeashedEntities().size();
            MoneyValue price = WaystonesNode.calculatePrice(player,target,mode,leashed);
            if(price.isEmpty())
                return;
            IMoneyHandler moneyHandler = MoneyAPI.API.GetPlayersMoneyHandler(player);
            if(!moneyHandler.getStoredMoney().containsValue(price))
                event.setCanceled(true);
            else
                warpDistanceCache.put(player.getUUID(),new WarpDistanceCache(player,target));
        }
    }

    @SubscribeEvent
    public static void afterTeleport(WaystoneTeleportEvent.Post event)
    {
        Entity entity = event.getContext().getEntity();
        if(entity instanceof Player player && !player.getAbilities().instabuild)
        {
            WarpDistanceCache distanceData = warpDistanceCache.getOrDefault(player.getUUID(),WarpDistanceCache.EMPTY);
            if(distanceData.isEmpty())
            {
                WaystonesNode.LOGGER.warn("Warp Distance was not cached for {}'s warp to {}",
                        player.getName().getString(),
                        event.getContext().getTargetWaystone().getName());
            }
            MoneyValue price = WaystonesNode.calculatePrice(event.getContext().getTargetWaystone(),event.getContext().getWarpMode(),event.getContext().getLeashedEntities().size(),distanceData);
            if(price.isEmpty())
                return;
            IMoneyHandler moneyHandler = MoneyAPI.API.GetPlayersMoneyHandler(player);
            moneyHandler.extractMoney(price,false);
        }
    }

    @SubscribeEvent
    public static void serverTick(TickEvent.ServerTickEvent event)
    {
        if(event.phase != TickEvent.Phase.END)
            return;
        if(event.getServer().getTickCount() % 1200 == 0)
        {
            List<UUID> removeList = new ArrayList<>();
            warpDistanceCache.forEach((id,data) -> {
                if(data.saveData)
                    data.saveData = false;
                else
                    removeList.add(id);
            });
            for(UUID remove : removeList)
                warpDistanceCache.remove(remove);
        }
    }

}
