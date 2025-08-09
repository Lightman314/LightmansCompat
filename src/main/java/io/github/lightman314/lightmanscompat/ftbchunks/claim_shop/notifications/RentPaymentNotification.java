package io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.notifications;

import com.google.common.collect.Lists;
import io.github.lightman314.lightmanscompat.LCompat;
import io.github.lightman314.lightmanscompat.ftbchunks.FTBChunksText;
import io.github.lightman314.lightmanscurrency.api.misc.player.PlayerReference;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.notifications.Notification;
import io.github.lightman314.lightmanscurrency.api.notifications.NotificationCategory;
import io.github.lightman314.lightmanscurrency.api.notifications.NotificationType;
import io.github.lightman314.lightmanscurrency.api.taxes.notifications.TaxableNotification;
import io.github.lightman314.lightmanscurrency.common.notifications.categories.TraderCategory;
import io.github.lightman314.lightmanscurrency.util.TimeUtil;
import io.github.lightman314.lightmanscurrency.util.VersionUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Supplier;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class RentPaymentNotification extends TaxableNotification {

    public static final NotificationType<RentPaymentNotification> TYPE = new NotificationType<>(VersionUtil.modResource(LCompat.MODID,"ftb_rent_payment"),RentPaymentNotification::new);

    private PlayerReference player;
    private MoneyValue price;
    private long timeRemaining;
    private TraderCategory category;

    private RentPaymentNotification() {}
    protected RentPaymentNotification(PlayerReference player, MoneyValue price, MoneyValue taxesPaid,long timeRemaining, TraderCategory category)
    {
        super(taxesPaid);
        this.player = player;
        this.price = price;
        this.timeRemaining = timeRemaining;
        this.category = category;
    }

    public static Supplier<Notification> of(PlayerReference player, MoneyValue price, MoneyValue taxesPaid, long timeRemaining, TraderCategory category) { return () -> new RentPaymentNotification(player,price,taxesPaid,timeRemaining,category); }

    @Override
    protected List<MutableComponent> getNormalMessageLines() {
        String timeRemainingText = new TimeUtil.TimeData(this.timeRemaining).getString();
        return Lists.newArrayList(
                FTBChunksText.NOTIFICATION_RENT_PAYMENT_1.get(this.player.getName(this.isClient()),this.price,timeRemainingText),
                FTBChunksText.NOTIFICATION_RENT_PAYMENT_2.get(this.player.getName(this.isClient()),this.price,timeRemainingText));
    }

    @Override
    protected NotificationType<?> getType() { return TYPE; }

    @Override
    public NotificationCategory getCategory() { return this.category; }

    @Override
    protected void saveNormal(CompoundTag tag, HolderLookup.Provider lookup) {

        tag.put("Player",this.player.save());
        tag.put("Price",this.price.save());
        tag.putLong("TimeRemaining",this.timeRemaining);
        tag.put("Trader",this.category.save(lookup));

    }

    @Override
    protected void loadNormal(CompoundTag tag, HolderLookup.Provider lookup) {

        this.player = PlayerReference.load(tag.getCompound("Player"));
        this.price = MoneyValue.load(tag.getCompound("Price"));
        this.timeRemaining = tag.getLong("TimeRemaining");
        this.category = new TraderCategory(tag,lookup);

    }

    //Time remaining is never going to match, so don't bother merging
    @Override
    protected boolean canMerge(Notification notification) { return false; }
}
