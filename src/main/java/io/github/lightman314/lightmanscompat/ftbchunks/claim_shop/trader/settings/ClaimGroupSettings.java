package io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.settings;

import io.github.lightman314.lightmanscompat.ftbchunks.FTBChunksText;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.ClaimShopData;
import io.github.lightman314.lightmanscurrency.api.settings.data.LoadContext;
import io.github.lightman314.lightmanscurrency.api.settings.data.SavedSettingData;
import io.github.lightman314.lightmanscurrency.api.traders.settings.TraderSettingsNode;
import io.github.lightman314.lightmanscurrency.common.traders.permissions.Permissions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.function.Consumer;

public class ClaimGroupSettings extends TraderSettingsNode<ClaimShopData> {

    public ClaimGroupSettings(ClaimShopData host) { super("claim_group", host); }

    @Override
    public MutableComponent getName() { return FTBChunksText.DATA_CATEGORY_CLAIM_SHOP_GROUP.get(); }

    @Override
    public boolean allowLoading(LoadContext context) { return context.hasPermission(Permissions.EDIT_SETTINGS); }

    @Override
    public void saveSettings(SavedSettingData.MutableNodeAccess nodeAccess) {
        nodeAccess.setStringValue("claim_group",this.trader.getClaimGroup());
        nodeAccess.setIntValue("claim_limit",this.trader.getGroupLimit());
    }

    @Override
    public void loadSettings(SavedSettingData.NodeAccess nodeAccess, LoadContext context) {
        this.trader.setClaimGroup(nodeAccess.getStringValue("claim_group"));
        this.trader.setGroupLimit(nodeAccess.getIntValue("claim_limit"));
    }

    @Override
    protected void writeLines(SavedSettingData.NodeAccess nodeAccess, Consumer<Component> consumer) {
        consumer.accept(formatEntry(FTBChunksText.DATA_ENTRY_CLAIM_SHOP_GROUP.get(),nodeAccess.getStringValue("claim_group")));
        consumer.accept(formatEntry(FTBChunksText.DATA_ENTRY_CLAIM_SHOP_LIMIT.get(),nodeAccess.getIntValue("claim_limit")));
    }

}