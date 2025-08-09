package io.github.lightman314.lightmanscompat.api.claimshop;

import io.github.lightman314.lightmanscurrency.api.misc.player.PlayerReference;
import io.github.lightman314.lightmanscurrency.common.util.IClientTracker;
import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.Nullable;

@MethodsReturnNonnullByDefault
public interface IGroupableClaimTrader extends IClientTracker {

    String getClaimGroup();
    @Nullable
    PlayerReference getCustomer();

}