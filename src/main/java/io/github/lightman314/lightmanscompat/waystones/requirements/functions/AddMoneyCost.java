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

public class AddMoneyCost implements RequirementFunction<MoneyRequirement,MoneyValueParameter> {

    public static final ResourceLocation ID = VersionUtil.modResource(LCompat.MODID,"add_money_cost");

    public static final AddMoneyCost INSTANCE = new AddMoneyCost();

    private AddMoneyCost() {}

    @Override
    public ResourceLocation getId() { return ID; }
    @Override
    public ResourceLocation getRequirementType() { return MoneyRequirement.ID; }
    @Override
    public Class<MoneyValueParameter> getParameterType() { return MoneyValueParameter.class; }
    @Override
    public boolean isEnabled() { return WaystonesConfig.getActive().teleports.enableCosts; }
    @Override
    public MoneyRequirement apply(MoneyRequirement moneyRequirement, WarpRequirementsContext context, MoneyValueParameter parameter) {
        MoneyValue newPrice = moneyRequirement.price().addValue(parameter.amount());
        if(newPrice == null)
            newPrice = parameter.amount();
        return new MoneyRequirement(newPrice);
    }

}
