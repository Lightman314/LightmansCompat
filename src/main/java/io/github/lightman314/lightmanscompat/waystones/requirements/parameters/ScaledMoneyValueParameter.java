package io.github.lightman314.lightmanscompat.waystones.requirements.parameters;

import com.mojang.brigadier.StringReader;
import io.github.lightman314.lightmanscompat.waystones.WaystonesNode;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValueParser;
import io.github.lightman314.lightmanscurrency.util.VersionUtil;
import net.blay09.mods.waystones.api.requirement.ParameterSerializer;
import net.minecraft.resources.ResourceLocation;

public record ScaledMoneyValueParameter(ResourceLocation data, float valuePerCost, MoneyValue price) {

    public static final ParameterSerializer<ScaledMoneyValueParameter> SERIALIZER = new Serializer();

    private static class Serializer implements ParameterSerializer<ScaledMoneyValueParameter>
    {
        @Override
        public Class<ScaledMoneyValueParameter> getType() { return ScaledMoneyValueParameter.class; }
        @Override
        public ScaledMoneyValueParameter deserialize(String s) {
            String[] split = s.split(",",3);
            if(split.length != 3)
                throw new IllegalArgumentException("Parameter count mismatch for type " + ScaledMoneyValueParameter.class);
            try {

                String id = split[0];
                ResourceLocation val1;
                if(!id.contains(":"))
                    val1 = VersionUtil.modResource(WaystonesNode.WAYSTONES_MODID,id);
                else
                    val1 = VersionUtil.parseResource(id);
                float val2 = Float.parseFloat(split[1]);
                MoneyValue val3 = MoneyValueParser.parse(new StringReader(split[2]),false);
                return new ScaledMoneyValueParameter(val1,val2,val3);
            } catch (Exception e) { throw new RuntimeException(e); }
        }
    }

}
