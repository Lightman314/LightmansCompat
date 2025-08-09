package io.github.lightman314.lightmanscompat.ftbchunks;

import io.github.lightman314.lightmanscompat.LCompat;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.notifications.*;
import io.github.lightman314.lightmanscompat.ftbchunks.core.FTBChunksBlocks;
import io.github.lightman314.lightmanscurrency.common.text.MultiLineTextEntry;
import io.github.lightman314.lightmanscurrency.common.text.TextEntry;

public class FTBChunksText {

    public static final TextEntry BLOCK_CLAIM_SHOP = TextEntry.block(FTBChunksBlocks.CLAIM_SHOP);

    public static final MultiLineTextEntry TOOLTIP_CLAIM_SHOP = MultiLineTextEntry.tooltip(LCompat.MODID,"ftb.claim_shop");

    public static final TextEntry NOTIFICATION_LAND_PURCHASE = TextEntry.notification(LandPurchaseNotification.TYPE);
    public static final TextEntry NOTIFICATION_RENT_PAYMENT_1 = TextEntry.notification(RentPaymentNotification.TYPE,"1");
    public static final TextEntry NOTIFICATION_RENT_PAYMENT_2 = TextEntry.notification(RentPaymentNotification.TYPE,"2");
    public static final TextEntry NOTIFICATION_RENT_DUE = TextEntry.notification(RentDueNotification.TYPE);
    public static final TextEntry NOTIFICATION_RENT_EXPIRED = TextEntry.notification(RentExpiredNotification.TYPE);

    public static final TextEntry TOOLTIP_CLAIM_SHOP_EDIT = TextEntry.tooltip(LCompat.MODID,"ftb.trader.claim_shop.edit");
    public static final TextEntry BUTTON_CLAIM_SHOP_RECLAIM_BLOCK = TextEntry.button(LCompat.MODID,"ftb.trader.claim_shop.reclaim_block");
    public static final TextEntry TOOLTIP_CLAIM_SHOP_RECLAIM_BLOCK = TextEntry.tooltip(LCompat.MODID,"ftb.trader.claim_shop.reclaim_block");
    public static final TextEntry GUI_CLAIM_SHOP_ACTIVE = TextEntry.gui(LCompat.MODID,"ftb.trader.claim_shop.active");
    public static final TextEntry GUI_CLAIM_SHOP_PRICE = TextEntry.gui(LCompat.MODID,"ftb.trader.claim_shop.price");
    public static final TextEntry GUI_CLAIM_SHOP_RENT_MODE = TextEntry.gui(LCompat.MODID,"ftb.trader.claim_shop.rent_mode");
    public static final TextEntry TOOLTIP_CLAIM_SHOP_RENT = TextEntry.tooltip(LCompat.MODID,"ftb.trader.claim_shop.rent");
    public static final TextEntry GUI_CLAIM_SHOP_MAX_RENT_PAYMENTS = TextEntry.gui(LCompat.MODID,"ftb.trader.claim_shop.max_rent_payments");
    public static final TextEntry GUI_CLAIM_SHOP_RENT_DURATION = TextEntry.gui(LCompat.MODID,"ftb.trader.claim_shop.rent_duration");
    public static final TextEntry GUI_CLAIM_SHOP_RENT_DURATION_EMPTY = TextEntry.gui(LCompat.MODID,"ftb.trader.claim_shop.rent_duration.empty");
    public static final TextEntry TOOLTIP_CLAIM_SHOP_MAP = TextEntry.tooltip(LCompat.MODID,"ftb.trader.claim_shop.map");
    public static final TextEntry GUI_CLAIM_SHOP_MAP_LABEL = TextEntry.gui(LCompat.MODID,"ftb.trader.claim_shop.map.label");
    public static final TextEntry TOOLTIP_CLAIM_SHOP_MAP_UNCLAIMED = TextEntry.tooltip(LCompat.MODID,"ftb.trader.claim_shop.map.unclaimed");
    public static final TextEntry TOOLTIP_CLAIM_SHOP_MAP_CLAIMED_BY_OTHER = TextEntry.tooltip(LCompat.MODID,"ftb.trader.claim_shop.map.claimed_by_other");
    public static final TextEntry TOOLTIP_CLAIM_SHOP_QUICK_CLAIM = TextEntry.tooltip(LCompat.MODID,"ftb.trader.claim_shop.quick_claim");
    public static final TextEntry TOOLTIP_CLAIM_SHOP_GROUP_SETTINGS = TextEntry.tooltip(LCompat.MODID,"ftb.trader.claim_shop.group_settings");
    public static final TextEntry GUI_CLAIM_SHOP_GROUP = TextEntry.gui(LCompat.MODID,"ftb.trader.claim_shop.group");
    public static final TextEntry GUI_CLAIM_SHOP_GROUP_LIMIT = TextEntry.gui(LCompat.MODID,"ftb.trader.claim_shop.group_limit");
    public static final TextEntry GUI_CLAIM_SHOP_GROUP_INFO = TextEntry.gui(LCompat.MODID,"ftb.trader.claim_shop.group.info");
    public static final TextEntry GUI_CLAIM_SHOP_GROUP_INFO_NO_KEY = TextEntry.gui(LCompat.MODID,"ftb.trader.claim_shop.group.info.no");
    public static final TextEntry GUI_CLAIM_SHOP_GROUP_INFO_NO_LIMITS = TextEntry.gui(LCompat.MODID,"ftb.trader.claim_shop.group.info.no_limits");

    //Customer Tab
    public static final TextEntry TOOLTIP_CLAIM_SHOP_CUSTOMER = TextEntry.tooltip(LCompat.MODID,"ftb.trader.claim_shop.customer");
    public static final TextEntry BUTTON_CLAIM_SHOP_RENT = TextEntry.button(LCompat.MODID,"ftb.trader.claim_shop.rent");
    public static final TextEntry BUTTON_CLAIM_SHOP_PURCHASE = TextEntry.button(LCompat.MODID,"ftb.trader.claim_shop.purchase");
    public static final TextEntry TOOLTIP_CLAIM_SHOP_NOT_ACTIVATED = TextEntry.tooltip(LCompat.MODID,"ftb.trader.claim_shop.not_activated");
    public static final TextEntry TOOLTIP_CLAIM_SHOP_RENT_PRICE = TextEntry.tooltip(LCompat.MODID,"ftb.trader.claim_shop.price.rent");
    public static final TextEntry TOOLTIP_CLAIM_SHOP_RENT_STATUS = TextEntry.tooltip(LCompat.MODID,"ftb.trader.claim_shop.rent_status");
    public static final TextEntry TOOLTIP_CLAIM_SHOP_PURCHASE_PRICE = TextEntry.tooltip(LCompat.MODID,"ftb.trader.claim_shop.price.purchase");
    public static final TextEntry TOOLTIP_CLAIM_SHOP_SAME_TEAM = TextEntry.tooltip(LCompat.MODID,"ftb.trader.claim_shop.same_team");
    public static final TextEntry TOOLTIP_CLAIM_SHOP_PURCHASED_BY_OTHER = TextEntry.tooltip(LCompat.MODID,"ftb.trader.claim_shop.purchased_by_other");
    public static final TextEntry TOOLTIP_CLAIM_SHOP_RENTED_BY_OTHER = TextEntry.tooltip(LCompat.MODID,"ftb.trader.claim_shop.rented_by_other");
    public static final TextEntry TOOLTIP_CLAIM_SHOP_RENT_LIMIT = TextEntry.tooltip(LCompat.MODID,"ftb.trader.claim_shop.rent_limit");
    public static final TextEntry TOOLTIP_CLAIM_SHOP_GROUP_LIMIT = TextEntry.tooltip(LCompat.MODID,"ftb.trader.claim_shop.group_limit");
    //Customer Settings Tab
    public static final TextEntry TOOLTIP_CLAIM_SHOP_CUSTOMER_SETTINGS = TextEntry.tooltip(LCompat.MODID,"ftb.trader.claim_shop.customer_settings");
    public static final TextEntry GUI_CLAIM_SHOP_RENT_STATUS_NOT_RENTED = TextEntry.gui(LCompat.MODID,"ftb.trader.claim_shop.rent_status.not_rented");
    public static final TextEntry GUI_CLAIM_SHOP_RENT_WARNING = TextEntry.gui(LCompat.MODID,"ftb.trader.claim_shop.rent_warning");
    public static final TextEntry GUI_CLAIM_SHOP_RENT_WARNING_NONE = TextEntry.gui(LCompat.MODID,"ftb.trader.claim_shop.rent_warning.none");
    public static final TextEntry BUTTON_CLAIM_SHOP_RENT_WARNING_CLEAR = TextEntry.button(LCompat.MODID,"ftb.trader.claim_shop.rent_warning.clear");
    public static final TextEntry TOOLTIP_CLAIM_SHOP_RENT_WARNING_CLEAR = TextEntry.tooltip(LCompat.MODID,"ftb.trader.claim_shop.rent_warning.clear");
    public static final TextEntry BUTTON_CLAIM_SHOP_END_RENT = TextEntry.button(LCompat.MODID,"ftb.trader.claim_shop.customer.end_rent");
    public static final MultiLineTextEntry TOOLTIP_CLAIM_SHOP_END_RENT = MultiLineTextEntry.tooltip(LCompat.MODID,"ftb.trader.claim_shop.customer.end_rent");

    public static final TextEntry MESSAGE_CHUNK_LOCKED = TextEntry.message(LCompat.MODID,"ftb.trader.claim_shop.chunk_locked");

    //Trader Settings
    public static final TextEntry DATA_CATEGORY_CLAIM_SHOP_TRADE = TextEntry.dataCategory(LCompat.MODID,"ftb.claim_shop.trade");
    public static final TextEntry DATA_ENTRY_CLAIM_SHOP_PRICE = TextEntry.dataName(LCompat.MODID,"ftb.claim_shop.price");
    public static final TextEntry DATA_ENTRY_CLAIM_SHOP_CHUNKS = TextEntry.dataName(LCompat.MODID,"ftb.claim_shop.chunks");
    public static final TextEntry DATA_ENTRY_CLAIM_SHOP_RENT_MODE = TextEntry.dataName(LCompat.MODID,"ftb.claim_shop.rent_mode");
    public static final TextEntry DATA_ENTRY_CLAIM_SHOP_RENT_DURATION = TextEntry.dataName(LCompat.MODID,"ftb.claim_shop.rent_duration");
    public static final TextEntry DATA_ENTRY_CLAIM_SHOP_RENT_MAX_PAYMENTS = TextEntry.dataName(LCompat.MODID,"ftb.claim_shop.rent_max_payments");

    public static final TextEntry DATA_CATEGORY_CLAIM_SHOP_GROUP = TextEntry.dataCategory(LCompat.MODID,"ftb.claim_shop.group_settings");
    public static final TextEntry DATA_ENTRY_CLAIM_SHOP_GROUP = TextEntry.dataName(LCompat.MODID,"ftb.claim_shop.group_settings.group");
    public static final TextEntry DATA_ENTRY_CLAIM_SHOP_LIMIT = TextEntry.dataName(LCompat.MODID,"ftb.claim_shop.group_settings.limit");

}

