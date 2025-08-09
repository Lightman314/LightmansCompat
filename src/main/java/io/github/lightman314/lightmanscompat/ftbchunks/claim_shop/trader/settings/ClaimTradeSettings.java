package io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.settings;

import io.github.lightman314.lightmanscompat.ftbchunks.FTBChunksText;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.ClaimShopData;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.settings.data.LoadContext;
import io.github.lightman314.lightmanscurrency.api.settings.data.SavedSettingData;
import io.github.lightman314.lightmanscurrency.api.traders.settings.TraderSettingsNode;
import io.github.lightman314.lightmanscurrency.common.traders.permissions.Permissions;
import io.github.lightman314.lightmanscurrency.util.TimeUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.ChunkPos;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class ClaimTradeSettings extends TraderSettingsNode<ClaimShopData> {

    public ClaimTradeSettings(ClaimShopData host) { super("trade", host); }

    @Override
    public MutableComponent getName() { return FTBChunksText.DATA_CATEGORY_CLAIM_SHOP_TRADE.get(); }

    @Override
    public boolean allowLoading(LoadContext context) { return context.hasPermission(Permissions.EDIT_TRADES); }

    @Override
    public void saveSettings(SavedSettingData.MutableNodeAccess nodeAccess) {
        nodeAccess.setCompoundValue("price",this.trader.getPrice().save());
        ChunkPos centerChunk = new ChunkPos(this.trader.getPos());
        int index = 0;
        for(ChunkPos chunk : this.trader.getChunkTargets())
            nodeAccess.setLongValue("chunk_" + index++,getOffset(chunk,centerChunk).toLong());
        nodeAccess.setBooleanValue("rent_mode",this.trader.isRentMode());
        nodeAccess.setLongValue("rent_duration",this.trader.getRentDuration());
        nodeAccess.setIntValue("rent_max_payments",this.trader.getMaxRentPayments());
    }

    private static ChunkPos getOffset(ChunkPos pos,ChunkPos center) { return new ChunkPos(pos.x - center.x,pos.z - center.z); }

    private static ChunkPos removeOffset(ChunkPos pos, ChunkPos center) { return new ChunkPos(pos.x + center.x,pos.z + center.z); }

    @Override
    public void loadSettings(SavedSettingData.NodeAccess nodeAccess, LoadContext context) {
        MoneyValue price = MoneyValue.load(nodeAccess.getCompoundValue("price"));
        if(price == null)
            price = MoneyValue.empty();
        this.trader.setPrice(price);
        //Only allow the price to be loaded if the trader is currently locked
        if(!this.trader.isLocked())
        {
            ChunkPos center = new ChunkPos(this.trader.getPos());
            Set<ChunkPos> targets = new HashSet<>();
            int index = 0;
            while(nodeAccess.hasLongValue("chunk_" + index))
                targets.add(removeOffset(new ChunkPos(nodeAccess.getLongValue("chunk_" + index)),center));
            this.trader.overrideChunkTargets(targets);

            this.trader.setRentMode(nodeAccess.getBooleanValue("rent_mode"));
            this.trader.setRentDuration(nodeAccess.getLongValue("rent_duration"));
            this.trader.setMaxRentPayments(nodeAccess.getIntValue("rent_max_payments"));

        }
    }

    @Override
    protected void writeLines(SavedSettingData.NodeAccess nodeAccess, Consumer<Component> consumer) {
        MoneyValue price = MoneyValue.load(nodeAccess.getCompoundValue("price"));
        consumer.accept(formatEntry(FTBChunksText.DATA_ENTRY_CLAIM_SHOP_PRICE.get(),price == null ? "NULL" : price.getString()));
        int chunkCount = 0;
        while(nodeAccess.hasLongValue("chunk_" + chunkCount))
            chunkCount++;
        consumer.accept(formatEntry(FTBChunksText.DATA_ENTRY_CLAIM_SHOP_CHUNKS.get(),chunkCount));
        consumer.accept(formatEntry(FTBChunksText.DATA_ENTRY_CLAIM_SHOP_RENT_MODE.get(),nodeAccess.getBooleanValue("rent_mode")));
        consumer.accept(formatEntry(FTBChunksText.DATA_ENTRY_CLAIM_SHOP_RENT_DURATION.get(),new TimeUtil.TimeData(nodeAccess.getLongValue("rent_duration")).getShortString()));
        consumer.accept(formatEntry(FTBChunksText.DATA_ENTRY_CLAIM_SHOP_RENT_MAX_PAYMENTS.get(),nodeAccess.getIntValue("rent_max_payments")));
    }

}