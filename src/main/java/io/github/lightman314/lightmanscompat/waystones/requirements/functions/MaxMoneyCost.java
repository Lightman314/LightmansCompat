package io.github.lightman314.lightmanscompat.waystones.requirements.functions;

import io.github.lightman314.lightmanscompat.LCompat;
import io.github.lightman314.lightmanscompat.waystones.requirements.MoneyRequirement;
import io.github.lightman314.lightmanscompat.waystones.requirements.parameters.MoneyValueParameter;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.util.VersionUtil;
import net.blay09.mods.waystones.api.requirement.RequirementFunction;
import net.blay09.mods.waystones.api.requirement.WarpRequirementsContext;
import net.blay09.mods.waystones.config.WaystonesConfig;
import net.minecraft.resources.ResourceLocation;

public class MaxMoneyCost implements RequirementFunction<MoneyRequirement,MoneyValueParameter> {

    public static final ResourceLocation ID = VersionUtil.modResource(LCompat.MODID,"max_money_cost");

    public static final MaxMoneyCost INSTANCE = new MaxMoneyCost();

    private MaxMoneyCost() {}

    @Override
    public ResourceLocation getId() { return ID; }
    @Override
    public ResourceLocation getRequirementType() { return MoneyRequirement.ID; }
    @Override
    public Class<MoneyValueParameter> getParameterType() { return MoneyValueParameter.class; }
    @Override
    public boolean isEnabled() { return WaystonesConfig.getActive().teleports.enableCosts; }
    @Override
    public MoneyRequirement apply(MoneyRequirement moneyRequirement, WarpRequirementsContext warpRequirementsContext, MoneyValueParameter parameter) {
        MoneyValue price = moneyRequirement.price();
        MoneyValue maxPrice = parameter.amount();
        if(maxPrice.sameType(price) && price.getCoreValue() > maxPrice.getCoreValue())
            return new MoneyRequirement(maxPrice);
        return moneyRequirement;
    }
}
