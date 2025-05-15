package io.github.lightman314.lightmanscompat.waystones.requirements.parameters;

import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValueParser;
import net.blay09.mods.waystones.api.requirement.ParameterSerializer;

public record MoneyValueParameter(MoneyValue amount) {

    public static final ParameterSerializer<MoneyValueParameter> SERIALIZER = new Serializer();

    private static class Serializer implements ParameterSerializer<MoneyValueParameter>
    {
        @Override
        public Class<MoneyValueParameter> getType() { return MoneyValueParameter.class; }
        @Override
        public MoneyValueParameter deserialize(String s) { return new MoneyValueParameter(MoneyValueParser.ParseConfigString(s,MoneyValue::empty)); }
    }

}
