package io.github.lightman314.lightmanscompat.ftbchunks.util;

import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.Team;
import dev.ftb.mods.ftbteams.api.TeamManager;
import dev.ftb.mods.ftbteams.api.client.ClientTeamManager;
import io.github.lightman314.lightmanscurrency.api.misc.player.PlayerReference;
import io.github.lightman314.lightmanscurrency.api.notifications.Notification;
import io.github.lightman314.lightmanscurrency.api.notifications.NotificationAPI;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class FTBTeamHelper {

    public static boolean isOnSameTeam(PlayerReference player1, PlayerReference player2, boolean isClient)
    {
        if(player1.is(player2))
            return true;
        Team team1 = getPlayersTeam(player1,isClient);
        Team team2 = getPlayersTeam(player2,isClient);
        return team1 != null && team2 != null && team1.getTeamId().equals(team2.getTeamId());
    }

    @Nullable
    public static Component getPlayersTeamName(PlayerReference player, boolean isClient)
    {
        //Client Only
        Team team = FTBTeamHelper.getPlayersTeam(player,isClient);
        return team == null ? null : team.getName();
    }

    @Nullable
    public static Team getPlayersTeam(PlayerReference player, boolean isClient)
    {
        if(isClient)
        {
            if(FTBTeamsAPI.api().isClientManagerLoaded())
            {
                ClientTeamManager manager = FTBTeamsAPI.api().getClientManager();
                AtomicReference<Team> result = new AtomicReference<>(null);
                manager.getKnownPlayer(player.id).ifPresent(p -> result.set(manager.getTeamByID(p.teamId()).orElse(null)));
                return result.get();
            }
        }
        else
        {
            if(FTBTeamsAPI.api().isManagerLoaded())
            {
                TeamManager manager = FTBTeamsAPI.api().getManager();
                return manager.getTeamForPlayerID(player.id).orElse(null);
            }
        }
        return null;
    }

    public static void pushNotificationToRenter(PlayerReference renter, Supplier<Notification> source)
    {
        Team team = getPlayersTeam(renter,false);
        if(team == null)
            return;
        for(UUID member : team.getMembers())
            NotificationAPI.API.PushPlayerNotification(member,source.get());
    }

}
