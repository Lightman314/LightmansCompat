package io.github.lightman314.lightmanscompat.api.claimshop;

import io.github.lightman314.lightmanscompat.LCompat;
import io.github.lightman314.lightmanscurrency.api.misc.player.PlayerReference;
import io.github.lightman314.lightmanscurrency.api.traders.TraderAPI;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ClaimGroupData {

    private static boolean initialized = false;
    private static final Map<String,Set<Long>> clientCache = new HashMap<>();
    private static final Map<String,Set<Long>> serverCache = new HashMap<>();

    public static void init() {
        if(initialized)
            return;
        NeoForge.EVENT_BUS.register(ClaimGroupData.class);
        initialized = true;
    }

    public static void updateCache(IGroupableClaimTrader trader, @Nullable String oldGroup)
    {
        Map<String,Set<Long>> cache = trader.isClient() ? clientCache : serverCache;
        long traderID;
        if(trader instanceof TraderData t)
        {
            traderID = t.getID();
            if(traderID < 0)
                return;
        }
        else
        {
            LCompat.LOGGER.error("Attempted to update the cache of a claim trader ({}) that isn't actually a trader!",trader.getClass().getName());
            return;
        }
        if(oldGroup != null && !oldGroup.isBlank())
        {
            Set<Long> set = cache.get(oldGroup);
            if(set != null)
            {
                set.remove(traderID);
                if(set.isEmpty())
                    cache.remove(oldGroup);
            }
        }
        String newGroup = trader.getClaimGroup();
        if(!newGroup.isBlank())
        {
            Set<Long> set = cache.getOrDefault(newGroup,new HashSet<>());
            set.add(traderID);
            cache.put(newGroup,set);
        }
    }

    public static int getCountForGroup(IGroupableClaimTrader trader, PlayerReference customer) { return getCountForGroup(trader.getClaimGroup(),customer,trader.isClient()); }
    public static int getCountForGroup(String group, PlayerReference customer, boolean isClient)
    {
        if(group.isBlank())
            return 0;
        Map<String,Set<Long>> cache = isClient ? clientCache : serverCache;
        int count = 0;
        Set<Long> removeIDs = new HashSet<>();
        Set<Long> cachedSet = cache.getOrDefault(group,new HashSet<>());
        for(long traderID : cachedSet)
        {
            TraderData t = TraderAPI.API.GetTrader(isClient,traderID);
            if(t instanceof IGroupableClaimTrader shop && shop.getClaimGroup().equals(group) && customer.is(shop.getCustomer()))
                count++;
            else //Remove the non-existent or invalid traders from the cache
                removeIDs.add(traderID);
        }
        cachedSet.removeAll(removeIDs);
        return count;
    }

    public static void clearClientCache() { clientCache.clear(); }
    //Stop events to clear the cache's when relevant
    @SubscribeEvent
    public static void onServerStop(ServerStoppedEvent event) { serverCache.clear(); }

}
