package io.github.lightman314.lightmanscompat.datagen.client.language;

import io.github.lightman314.lightmanscompat.LCompat;
import io.github.lightman314.lightmanscompat.waystones.WaystonesText;
import io.github.lightman314.lightmanscurrency.datagen.client.language.TranslationProvider;
import net.minecraft.data.PackOutput;

public class EnglishProvider extends TranslationProvider {

    public EnglishProvider(PackOutput output) { super(output, LCompat.MODID,"en_us"); }

    @Override
    protected void addTranslations() {

        //Money Cost tooltip
        this.translate(WaystonesText.TOOLTIP_WAYSTONE_MONEY_COST,"Warping Costs %s");

    }

}
