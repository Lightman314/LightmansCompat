package io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.notifications;

import io.github.lightman314.lightmanscompat.LCompat;
import io.github.lightman314.lightmanscompat.ftbchunks.FTBChunksText;
import io.github.lightman314.lightmanscurrency.api.notifications.Notification;
import io.github.lightman314.lightmanscurrency.api.notifications.NotificationCategory;
import io.github.lightman314.lightmanscurrency.api.notifications.NotificationType;
import io.github.lightman314.lightmanscurrency.api.notifications.SingleLineNotification;
import io.github.lightman314.lightmanscurrency.common.notifications.categories.TraderCategory;
import io.github.lightman314.lightmanscurrency.util.TimeUtil;
import io.github.lightman314.lightmanscurrency.util.VersionUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class RentDueNotification extends SingleLineNotification {

    public static final NotificationType<RentDueNotification> TYPE = new NotificationType<>(VersionUtil.modResource(LCompat.MODID,"rent_due"),RentDueNotification::new);

    private long timeRemaining = 0;
    private TraderCategory category;
    protected RentDueNotification() {}
    private RentDueNotification(long timeRemaining, TraderCategory trader)
    {
        this.timeRemaining = timeRemaining;
        this.category = trader;
    }

    public static Supplier<Notification> of(long timeRemaining, TraderCategory trader) { return () -> new RentDueNotification(timeRemaining,trader); }

    @Override
    protected MutableComponent getMessage() { return FTBChunksText.NOTIFICATION_RENT_DUE.get(new TimeUtil.TimeData(this.timeRemaining).getString()); }

    @Override
    protected NotificationType<?> getType() { return TYPE; }

    @Override
    public NotificationCategory getCategory() { return this.category; }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider lookup) {
        tag.putLong("TimeRemaining",this.timeRemaining);
        tag.put("Trader",this.category.save(lookup));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider lookup) {
        this.timeRemaining = tag.getLong("TimeRemaining");
        this.category = new TraderCategory(tag.getCompound("Trader"),lookup);
    }

    //Don't merge rent due notifications
    @Override
    protected boolean canMerge(Notification notification) { return false; }

}
