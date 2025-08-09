package io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.notifications;

import io.github.lightman314.lightmanscompat.LCompat;
import io.github.lightman314.lightmanscompat.ftbchunks.FTBChunksText;
import io.github.lightman314.lightmanscurrency.api.misc.player.PlayerReference;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.notifications.Notification;
import io.github.lightman314.lightmanscurrency.api.notifications.NotificationCategory;
import io.github.lightman314.lightmanscurrency.api.notifications.NotificationType;
import io.github.lightman314.lightmanscurrency.api.taxes.notifications.SingleLineTaxableNotification;
import io.github.lightman314.lightmanscurrency.common.notifications.categories.TraderCategory;
import io.github.lightman314.lightmanscurrency.util.VersionUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class LandPurchaseNotification extends SingleLineTaxableNotification {

    public static final NotificationType<LandPurchaseNotification> TYPE = new NotificationType<>(VersionUtil.modResource(LCompat.MODID,"ftb_land_purchase"),LandPurchaseNotification::new);

    private PlayerReference player;
    private MoneyValue price;
    private TraderCategory category;

    private LandPurchaseNotification() {}
    protected LandPurchaseNotification(PlayerReference player, MoneyValue price, MoneyValue taxesPaid, TraderCategory category)
    {
        super(taxesPaid);
        this.player = player;
        this.price = price;
        this.category = category;
    }

    public static Supplier<Notification> of(PlayerReference player, MoneyValue price, MoneyValue taxesPaid, TraderCategory traderCategory) { return () -> new LandPurchaseNotification(player,price,taxesPaid,traderCategory); }

    @Override
    protected MutableComponent getNormalMessage() {
        return FTBChunksText.NOTIFICATION_LAND_PURCHASE.get(this.player.getName(this.isClient()),this.price);
    }

    @Override
    protected void saveNormal(CompoundTag tag) {
        tag.put("Player",this.player.save());
        tag.put("Price",this.price.save());
        tag.put("Category",this.category.save());
    }

    @Override
    protected void loadNormal(CompoundTag tag) {
        this.player = PlayerReference.load(tag.getCompound("Player"));
        this.price = MoneyValue.load(tag.getCompound("Price"));
        this.category = new TraderCategory(tag.getCompound("Category"));
    }

    @Override
    protected NotificationType<?> getType() { return TYPE; }

    @Override
    public NotificationCategory getCategory() { return this.category; }

    @Override
    protected boolean canMerge(Notification notification) { return false; }

}