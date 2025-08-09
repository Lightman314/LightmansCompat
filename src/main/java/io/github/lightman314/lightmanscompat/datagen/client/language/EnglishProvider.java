package io.github.lightman314.lightmanscompat.datagen.client.language;

import io.github.lightman314.lightmanscompat.LCompat;
import io.github.lightman314.lightmanscompat.ftbchunks.FTBChunksText;
import io.github.lightman314.lightmanscompat.waystones.WaystonesText;
import io.github.lightman314.lightmanscurrency.datagen.client.language.TranslationProvider;
import net.minecraft.data.PackOutput;

public class EnglishProvider extends TranslationProvider {

    public EnglishProvider(PackOutput output) { super(output, LCompat.MODID,"en_us"); }

    @Override
    protected void addTranslations() {

        /// Waystones
        //Money Cost tooltip
        this.translate(WaystonesText.TOOLTIP_WAYSTONE_MONEY_COST,"Warping Costs %s");

        /// FTB Chunks
        this.translate(FTBChunksText.BLOCK_CLAIM_SHOP,"Claim Shop");

        this.translate(FTBChunksText.TOOLTIP_CLAIM_SHOP,"FTB Claim Shop:","Can be used to Sell or Rent a small group of FTB claim chunks to other players","Warning: Block cannot be destroyed or removed while rented","One-time sales (not renting) can only be done once per Claim Shop (breaking the block will reset this limit)");

        this.translate(FTBChunksText.NOTIFICATION_LAND_PURCHASE,"%1$s purchased land for %2$s");
        this.translate(FTBChunksText.NOTIFICATION_RENT_PAYMENT_1,"%1$s paid %2$s in rent");
        this.translate(FTBChunksText.NOTIFICATION_RENT_PAYMENT_2,"%3$s remaining until the next rent payment is due");
        this.translate(FTBChunksText.NOTIFICATION_RENT_DUE,"%s remaining until the next rent payment is due");
        this.translate(FTBChunksText.NOTIFICATION_RENT_EXPIRED,"%s remaining until the next rent payment is due");

        this.translate(FTBChunksText.TOOLTIP_CLAIM_SHOP_EDIT,"Trade Settings");
        this.translate(FTBChunksText.BUTTON_CLAIM_SHOP_RECLAIM_BLOCK,"Reclaim Trader");
        this.translate(FTBChunksText.TOOLTIP_CLAIM_SHOP_RECLAIM_BLOCK,"Allows you to reclaim the trader from another players claim after a one-time purchase");
        this.translate(FTBChunksText.GUI_CLAIM_SHOP_ACTIVE,"Active");
        this.translate(FTBChunksText.GUI_CLAIM_SHOP_PRICE,"Price:");
        this.translate(FTBChunksText.GUI_CLAIM_SHOP_RENT_MODE,"Rent Mode");
        this.translate(FTBChunksText.TOOLTIP_CLAIM_SHOP_RENT,"Rent Settings");
        this.translate(FTBChunksText.GUI_CLAIM_SHOP_MAX_RENT_PAYMENTS,"Max Early Rent Payments: %s");
        this.translate(FTBChunksText.GUI_CLAIM_SHOP_RENT_DURATION,"Rent Timer will be extended by %s per payment");
        this.translate(FTBChunksText.GUI_CLAIM_SHOP_RENT_DURATION_EMPTY,"Warning: Invalid Rent Duration is defined!");
        this.translate(FTBChunksText.TOOLTIP_CLAIM_SHOP_MAP,"Chunk Map");
        this.translate(FTBChunksText.GUI_CLAIM_SHOP_MAP_LABEL,"Chunks to Sell/Rent:");
        this.translate(FTBChunksText.TOOLTIP_CLAIM_SHOP_MAP_UNCLAIMED,"Not yet claimed! Cannot be sold in this state!");
        this.translate(FTBChunksText.TOOLTIP_CLAIM_SHOP_MAP_CLAIMED_BY_OTHER,"Owned by '%s', cannot be sold!");
        this.translate(FTBChunksText.TOOLTIP_CLAIM_SHOP_QUICK_CLAIM,"Quick-Claim Selected Chunks");
        this.translate(FTBChunksText.TOOLTIP_CLAIM_SHOP_GROUP_SETTINGS,"Claim Group Settings");
        this.translate(FTBChunksText.GUI_CLAIM_SHOP_GROUP,"Claim Group Key:");
        this.translate(FTBChunksText.GUI_CLAIM_SHOP_GROUP_LIMIT,"Group Limit:");
        this.translate(FTBChunksText.GUI_CLAIM_SHOP_GROUP_INFO,"A single customer may only own/rent %2$s land claims in group '%1$s'");
        this.translate(FTBChunksText.GUI_CLAIM_SHOP_GROUP_INFO_NO_KEY,"No Claim Group limitations will be applied to this claim shop");
        this.translate(FTBChunksText.GUI_CLAIM_SHOP_GROUP_INFO_NO_LIMITS,"No Claim Group limitations will be applied to this claim shop, but this shops customer will still count towards land claim limits for group '%s'");

        //Customer Tab
        this.translate(FTBChunksText.TOOLTIP_CLAIM_SHOP_CUSTOMER,"Customer Interaction");
        this.translate(FTBChunksText.BUTTON_CLAIM_SHOP_RENT,"Rent %s Chunk(s)");
        this.translate(FTBChunksText.BUTTON_CLAIM_SHOP_PURCHASE,"Purchase %s Chunk(s)");
        this.translate(FTBChunksText.TOOLTIP_CLAIM_SHOP_NOT_ACTIVATED,"This shop is not activated");
        this.translate(FTBChunksText.TOOLTIP_CLAIM_SHOP_RENT_PRICE,"Costs %1$s to rent for %2$s");
        this.translate(FTBChunksText.TOOLTIP_CLAIM_SHOP_RENT_STATUS,"Rented by %1$s for another %2$s");
        this.translate(FTBChunksText.TOOLTIP_CLAIM_SHOP_SAME_TEAM,"Your team already owns this land");
        this.translate(FTBChunksText.TOOLTIP_CLAIM_SHOP_PURCHASED_BY_OTHER,"This land has already been purchased by another team");
        this.translate(FTBChunksText.TOOLTIP_CLAIM_SHOP_RENTED_BY_OTHER,"This land has been rented for %s by another team");
        this.translate(FTBChunksText.TOOLTIP_CLAIM_SHOP_RENT_LIMIT,"You have reached the maximum early payment limit of %s");
        this.translate(FTBChunksText.TOOLTIP_CLAIM_SHOP_GROUP_LIMIT,"You have reached the maximum amount of land purchases of %s for this area");
        //Customer Settings Tab
        this.translate(FTBChunksText.TOOLTIP_CLAIM_SHOP_CUSTOMER_SETTINGS,"Customer Settings");
        this.translate(FTBChunksText.GUI_CLAIM_SHOP_RENT_STATUS_NOT_RENTED,"This land is not currently being rented by anyone");
        this.translate(FTBChunksText.GUI_CLAIM_SHOP_RENT_WARNING,"A notification will be posted to the customer(s) %s before rent is due");
        this.translate(FTBChunksText.GUI_CLAIM_SHOP_RENT_WARNING_NONE,"No alerts remining the customer about pending rent will be given");
        this.translate(FTBChunksText.BUTTON_CLAIM_SHOP_RENT_WARNING_CLEAR,"Clear Warning Flag");
        this.translate(FTBChunksText.TOOLTIP_CLAIM_SHOP_RENT_WARNING_CLEAR,"May result in a duplicate warning being given if the warning has already been given");
        //Admin Override Tab
        this.translate(FTBChunksText.BUTTON_CLAIM_SHOP_END_RENT,"End Current Rental");
        this.translate(FTBChunksText.TOOLTIP_CLAIM_SHOP_END_RENT,"Ends the current rental period and will attempt to return the land back to its original owner","Does not provide any form of refund to compensate for the time lost");

        this.translate(FTBChunksText.MESSAGE_CHUNK_LOCKED,"Chunk is locked by a Claim Shop and cannot be interacted with!");

        this.translate(FTBChunksText.DATA_CATEGORY_CLAIM_SHOP_TRADE,"Trade");
        this.translate(FTBChunksText.DATA_ENTRY_CLAIM_SHOP_PRICE,"Price");
        this.translate(FTBChunksText.DATA_ENTRY_CLAIM_SHOP_CHUNKS,"Chunks");
        this.translate(FTBChunksText.DATA_ENTRY_CLAIM_SHOP_RENT_MODE,"Rent Mode");
        this.translate(FTBChunksText.DATA_ENTRY_CLAIM_SHOP_RENT_DURATION,"Rent Duration");
        this.translate(FTBChunksText.DATA_ENTRY_CLAIM_SHOP_RENT_MAX_PAYMENTS,"Max Rent Payments");

        this.translate(FTBChunksText.DATA_CATEGORY_CLAIM_SHOP_GROUP,"Claim Group");
        this.translate(FTBChunksText.DATA_ENTRY_CLAIM_SHOP_GROUP,"Group");
        this.translate(FTBChunksText.DATA_ENTRY_CLAIM_SHOP_LIMIT,"Claim Limit");

    }

}
