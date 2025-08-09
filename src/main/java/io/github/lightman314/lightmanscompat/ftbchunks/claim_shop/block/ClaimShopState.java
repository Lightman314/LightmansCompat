package io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.block;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import javax.annotation.Nonnull;
import java.util.Locale;

public enum ClaimShopState implements StringRepresentable {
    INACTIVE,FOR_SALE,FOR_RENT,SOLD,RENTED;
    public static final EnumProperty<ClaimShopState> PROPERTY = EnumProperty.create("claim_state", ClaimShopState.class);
    @Nonnull
    @Override
    public String getSerializedName() { return this.toString().toLowerCase(Locale.ROOT); }
}
