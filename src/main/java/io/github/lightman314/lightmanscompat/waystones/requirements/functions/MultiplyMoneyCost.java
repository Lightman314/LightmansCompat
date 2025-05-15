package io.github.lightman314.lightmanscompat.waystones.requirements.functions;

import io.github.lightman314.lightmanscompat.LCompat;
import io.github.lightman314.lightmanscompat.waystones.requirements.MoneyRequirement;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.util.VersionUtil;
import net.blay09.mods.waystones.api.requirement.RequirementFunction;
import net.blay09.mods.waystones.api.requirement.WarpRequirementsContext;
import net.blay09.mods.waystones.config.WaystonesConfig;
import net.blay09.mods.waystones.requirement.RequirementRegistry;
import net.minecraft.resources.ResourceLocation;

public class MultiplyMoneyCost implements RequirementFunction<MoneyRequirement, RequirementRegistry.FloatParameter> {

    public static final ResourceLocation ID = VersionUtil.modResource(LCompat.MODID,"multiply_money_cost");

    public static final MultiplyMoneyCost INSTANCE = new MultiplyMoneyCost();

    private MultiplyMoneyCost() {}

    @Override
    public ResourceLocation getId() { return ID; }
    @Override
    public ResourceLocation getRequirementType() { return MoneyRequirement.ID; }
    @Override
    public Class<RequirementRegistry.FloatParameter> getParameterType() { return RequirementRegistry.FloatParameter.class; }
    @Override
    public boolean isEnabled() { return WaystonesConfig.getActive().teleports.enableCosts; }
    @Override
    public MoneyRequirement apply(MoneyRequirement moneyRequirement, WarpRequirementsContext warpRequirementsContext, RequirementRegistry.FloatParameter parameter) {
        MoneyValue price = moneyRequirement.price();
        if(price.isEmpty())
            return moneyRequirement;
        MoneyValue newPrice = price.multiplyValue(parameter.value());
        return new MoneyRequirement(newPrice);
    }
}
