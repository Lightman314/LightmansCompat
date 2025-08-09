package io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.notifications;

import io.github.lightman314.lightmanscompat.LCompat;
import io.github.lightman314.lightmanscompat.ftbchunks.FTBChunksText;
import io.github.lightman314.lightmanscurrency.api.misc.player.PlayerReference;
import io.github.lightman314.lightmanscurrency.api.notifications.Notification;
import io.github.lightman314.lightmanscurrency.api.notifications.NotificationCategory;
import io.github.lightman314.lightmanscurrency.api.notifications.NotificationType;
import io.github.lightman314.lightmanscurrency.api.notifications.SingleLineNotification;
import io.github.lightman314.lightmanscurrency.common.notifications.categories.TraderCategory;
import io.github.lightman314.lightmanscurrency.util.VersionUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class RentExpiredNotification extends SingleLineNotification {

    public static final NotificationType<RentExpiredNotification> TYPE = new NotificationType<>(VersionUtil.modResource(LCompat.MODID,"rent_expired"),RentExpiredNotification::new);

    PlayerReference renter;
    TraderCategory trader;
    protected RentExpiredNotification() {}
    private RentExpiredNotification(PlayerReference renter,TraderCategory trader){
        this.renter = renter;
        this.trader = trader;
    }

    public static Supplier<Notification> of(PlayerReference renter,TraderCategory trader) { return () -> new RentExpiredNotification(renter,trader); }

    @Override
    protected MutableComponent getMessage() { return FTBChunksText.NOTIFICATION_RENT_EXPIRED.get(); }
    @Override
    protected NotificationType<?> getType() { return TYPE; }
    @Override
    public NotificationCategory getCategory() { return this.trader; }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider lookup) {
        tag.put("Renter",this.renter.save());
        tag.put("Trader",this.trader.save(lookup));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider lookup) {
        this.renter = PlayerReference.load(tag.getCompound("Renter"));
        this.trader = new TraderCategory(tag.getCompound("Trader"),lookup);
    }

    @Override
    protected boolean canMerge(Notification notification) { return false; }

}
