package io.github.lightman314.lightmanscompat.waystones.requirements.functions;

import io.github.lightman314.lightmanscompat.LCompat;
import io.github.lightman314.lightmanscompat.waystones.requirements.MoneyRequirement;
import io.github.lightman314.lightmanscompat.waystones.requirements.parameters.ScaledMoneyValueParameter;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.util.VersionUtil;
import net.blay09.mods.waystones.api.requirement.RequirementFunction;
import net.blay09.mods.waystones.api.requirement.WarpRequirementsContext;
import net.blay09.mods.waystones.config.WaystonesConfig;
import net.minecraft.resources.ResourceLocation;

public class ScaledAddMoneyCost implements RequirementFunction<MoneyRequirement,ScaledMoneyValueParameter> {

    public static final ResourceLocation ID = VersionUtil.modResource(LCompat.MODID,"scaled_add_money_cost");

    public static final ScaledAddMoneyCost INSTANCE = new ScaledAddMoneyCost();

    private ScaledAddMoneyCost() {}

    @Override
    public ResourceLocation getId() { return ID; }
    @Override
    public ResourceLocation getRequirementType() { return MoneyRequirement.ID; }
    @Override
    public Class<ScaledMoneyValueParameter> getParameterType() { return ScaledMoneyValueParameter.class; }
    @Override
    public boolean isEnabled() { return WaystonesConfig.getActive().teleports.enableCosts; }
    @Override
    public MoneyRequirement apply(MoneyRequirement moneyRequirement, WarpRequirementsContext context, ScaledMoneyValueParameter parameter) {
        float value = context.getContextValue(parameter.data());
        double mult = (double)value / (double)parameter.valuePerCost();
        MoneyValue addPrice = parameter.price().multiplyValue(mult);
        MoneyValue originalPrice = moneyRequirement.price();
        MoneyValue newPrice = originalPrice.addValue(addPrice);
        if(newPrice == null)
            newPrice = addPrice;
        return new MoneyRequirement(newPrice);
    }

}
