package io.github.lightman314.lightmanscompat.waystones.requirements;

import io.github.lightman314.lightmanscompat.LCompat;
import io.github.lightman314.lightmanscompat.waystones.WaystonesText;
import io.github.lightman314.lightmanscurrency.api.capability.money.IMoneyHandler;
import io.github.lightman314.lightmanscurrency.api.money.MoneyAPI;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.util.VersionUtil;
import net.blay09.mods.waystones.api.requirement.RequirementType;
import net.blay09.mods.waystones.api.requirement.WarpRequirement;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public record MoneyRequirement(MoneyValue price) implements WarpRequirement {

    public static final ResourceLocation ID = VersionUtil.modResource(LCompat.MODID,"money_requirement");
    public static final RequirementType<MoneyRequirement> TYPE = new Type();

    public MoneyRequirement() { this(MoneyValue.empty()); }

    @Override
    public boolean canAfford(Player player) {
        IMoneyHandler handler = MoneyAPI.API.GetPlayersMoneyHandler(player);
        return handler.getStoredMoney().containsValue(this.price);
    }

    @Override
    public void consume(Player player) {
        IMoneyHandler handler = MoneyAPI.API.GetPlayersMoneyHandler(player);
        handler.extractMoney(this.price, false);
    }

    @Override
    public void rollback(Player player) {
        IMoneyHandler handler = MoneyAPI.API.GetPlayersMoneyHandler(player);
        handler.insertMoney(this.price, false);
    }

    @Override
    public void appendHoverText(Player player, List<Component> tooltip) {
        if(this.price.isEmpty())
            return;
        tooltip.add(WaystonesText.TOOLTIP_WAYSTONE_MONEY_COST.get(this.price.getText()));
    }

    @Override
    public boolean isEmpty() { return this.price.isEmpty(); }

    private static class Type implements RequirementType<MoneyRequirement>
    {
        private Type() {}
        @Override
        public ResourceLocation getId() { return ID; }
        @Override
        public MoneyRequirement createInstance() { return new MoneyRequirement(); }
    }

}
