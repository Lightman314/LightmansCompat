package io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.client.menu.trader_storage;

import io.github.lightman314.lightmanscompat.ftbchunks.FTBChunksText;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.ClaimShopData;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.menu.trader_storage.ClaimMapTab;
import io.github.lightman314.lightmanscompat.api.client.widgets.map.ChunkFrame;
import io.github.lightman314.lightmanscompat.api.client.widgets.map.MapRegionWidget;
import io.github.lightman314.lightmanscompat.ftbchunks.util.FTBChunksHelper;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.traders.menu.storage.TraderStorageClientTab;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyAddonHelper;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.client.util.TextRenderUtil;
import io.github.lightman314.lightmanscurrency.common.util.IconData;
import io.github.lightman314.lightmanscurrency.common.util.IconUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class ClaimMapClientTab extends TraderStorageClientTab<ClaimMapTab> {

    public ClaimMapClientTab(Object screen, ClaimMapTab commonTab) { super(screen, commonTab); }

    private MapRegionWidget map;

    @Override
    protected void initialize(ScreenArea screenArea, boolean firstOpen) {

        if(this.map != null)
            this.map.discard();
        if(this.menu.getTrader() instanceof ClaimShopData shop)
        {
            this.map = this.addChild(MapRegionWidget.builder()
                    .position(screenArea.pos.offset((screenArea.width / 2) - (9 + ClaimShopData.CLAIM_RANGE * 18),13))
                    .withRadius(ClaimShopData.CLAIM_RANGE)
                    .withCenterChunk(shop.getWorldPosition())
                    .withFrame(this::getFrame)
                    .withTooltip(this::getTooltip)
                    .withHandler(this::toggleSelection)
                    .addon(EasyAddonHelper.activeCheck(this.commonTab::isNotLocked))
                    .build());

            this.addChild(IconButton.builder()
                    .position(screenArea.pos.offset(screenArea.width - 30,13))
                    .pressAction(this.commonTab::TryQuickClaim)
                    .icon(IconUtil.ICON_ALEX_HEAD)
                    .addon(EasyAddonHelper.activeCheck(() -> this.commonTab.isNotLocked() && this.hasUnclaimedChunk()))
                    .addon(EasyAddonHelper.tooltip(FTBChunksText.TOOLTIP_CLAIM_SHOP_QUICK_CLAIM))
                    .build());

        }

    }

    @Override
    public void renderBG(@Nonnull EasyGuiGraphics gui) {

        //Render map label
        TextRenderUtil.drawCenteredText(gui,FTBChunksText.GUI_CLAIM_SHOP_MAP_LABEL.get(),this.screen.getXSize() / 2,4,0x404040);

    }

    @Override
    protected void closeAction() {
        if(this.map != null)
            this.map.discard();
    }

    private boolean isSelected(ChunkPos chunk)
    {
        if(this.menu.getTrader() instanceof ClaimShopData shop)
            return shop.getChunkTargets().contains(chunk);
        return false;
    }

    private ChunkFrame getFrame(ChunkPos chunk)
    {
        if(this.menu.getTrader() instanceof ClaimShopData shop && shop.getChunkTargets().contains(chunk))
        {
            ResourceKey<Level> dimension = shop.getWorldPosition().getDimension();
            if(dimension == null)
                return ChunkFrame.ERROR;
            if(FTBChunksHelper.isChunkOwner(shop.getOwner().getValidOwner().asPlayerReference(),dimension,chunk,true))
                return ChunkFrame.GOOD;
            else
            {
                UUID otherOwner = FTBChunksHelper.getChunkOwnerID(dimension,chunk,true);
                return otherOwner == null ? ChunkFrame.WARNING : ChunkFrame.ERROR;
            }
        }
        return ChunkFrame.NEUTRAL;
    }

    @Nullable
    private Component getTooltip(ChunkPos chunk)
    {
        if(this.menu.getTrader() instanceof ClaimShopData shop && shop.getChunkTargets().contains(chunk))
        {
            ResourceKey<Level> dimension = shop.getWorldPosition().getDimension();
            if(dimension == null)
                return null;
            if(FTBChunksHelper.isChunkOwner(shop.getOwner().getPlayerForContext(),dimension,chunk,true))
                return null;
            else
            {
                Component otherOwner = FTBChunksHelper.getChunkOwnerName(dimension,chunk,true);
                if(otherOwner != null)
                    return FTBChunksText.TOOLTIP_CLAIM_SHOP_MAP_CLAIMED_BY_OTHER.get(otherOwner);
                else
                    return FTBChunksText.TOOLTIP_CLAIM_SHOP_MAP_UNCLAIMED.get();
            }
        }
        return null;
    }

    private void toggleSelection(ChunkPos chunk)
    {
        if(this.isSelected(chunk))
            this.commonTab.DeselectChunk(chunk);
        else
            this.commonTab.SelectChunk(chunk);
    }

    private boolean hasUnclaimedChunk()
    {
        if(this.menu.getTrader() instanceof ClaimShopData shop)
        {
            ResourceKey<Level> dimension = shop.getWorldPosition().getDimension();
            if(dimension == null)
                return false;
            for(ChunkPos chunk : shop.getChunkTargets())
            {
                if(FTBChunksHelper.getChunkOwnerID(dimension,chunk,true) == null)
                    return true;
            }
        }
        return false;
    }

    @Nonnull
    @Override
    public IconData getIcon() { return IconData.of(Items.FILLED_MAP); }

    @Nullable
    @Override
    public Component getTooltip() { return FTBChunksText.TOOLTIP_CLAIM_SHOP_MAP.get(); }

}
