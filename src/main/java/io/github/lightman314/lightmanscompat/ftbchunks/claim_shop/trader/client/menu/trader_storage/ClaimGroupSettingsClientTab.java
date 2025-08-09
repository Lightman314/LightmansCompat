package io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.client.menu.trader_storage;

import io.github.lightman314.lightmanscompat.ftbchunks.FTBChunksText;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.ClaimShopData;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.menu.trader_storage.ClaimGroupSettingsTab;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.traders.menu.storage.TraderStorageClientTab;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.client.util.TextRenderUtil;
import io.github.lightman314.lightmanscurrency.client.util.text_inputs.IntParser;
import io.github.lightman314.lightmanscurrency.client.util.text_inputs.TextInputUtil;
import io.github.lightman314.lightmanscurrency.common.util.IconData;
import io.github.lightman314.lightmanscurrency.common.util.IconUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ClaimGroupSettingsClientTab extends TraderStorageClientTab<ClaimGroupSettingsTab> {

    public ClaimGroupSettingsClientTab(Object screen, ClaimGroupSettingsTab commonTab) { super(screen, commonTab); }

    @Override
    protected void initialize(ScreenArea screenArea, boolean firstOpen) {

        ClaimShopData shop = null;
        if(this.menu.getTrader() instanceof ClaimShopData s)
            shop = s;

        this.addChild(TextInputUtil.stringBuilder()
                .position(screenArea.pos.offset(30,20))
                .width(screenArea.width - 60)
                .maxLength(16)
                .startingValue(shop == null ? "" : shop.getClaimGroup())
                .handler(this.commonTab::setGroup)
                .build());

        this.addChild(TextInputUtil.intBuilder()
                .position(screenArea.pos.offset(30,60))
                .width(30)
                .maxLength(3)
                .apply(IntParser.builder().min(0).consumer())
                .startingValue(shop == null ? 0 : shop.getGroupLimit())
                .handler(this.commonTab::setLimit)
                .build());

    }

    @Override
    public void renderBG(EasyGuiGraphics gui) {

        //Group Label
        gui.drawString(FTBChunksText.GUI_CLAIM_SHOP_GROUP.get(),30,10,0x404040);
        //Group Limit Label
        gui.drawString(FTBChunksText.GUI_CLAIM_SHOP_GROUP_LIMIT.get(),30,50,0x404040);

        //Info Text
        if(this.menu.getTrader() instanceof ClaimShopData shop)
        {
            Component text;
            if(shop.getClaimGroup().isBlank())
                text = FTBChunksText.GUI_CLAIM_SHOP_GROUP_INFO_NO_KEY.get();
            else
            {
                if(shop.getGroupLimit() > 0)
                    text = FTBChunksText.GUI_CLAIM_SHOP_GROUP_INFO.get(shop.getClaimGroup(),shop.getGroupLimit());
                else
                    text = FTBChunksText.GUI_CLAIM_SHOP_GROUP_INFO_NO_LIMITS.get(shop.getClaimGroup());
            }
            TextRenderUtil.drawCenteredMultilineText(gui,text,30,this.screen.getXSize() - 60, 90, 0x404040);
        }

    }
    
    @Override
    public IconData getIcon() { return IconUtil.ICON_COUNT; }

    @Nullable
    @Override
    public Component getTooltip() { return FTBChunksText.TOOLTIP_CLAIM_SHOP_GROUP_SETTINGS.get(); }

    @Override
    public boolean blockInventoryClosing() { return true; }

}
