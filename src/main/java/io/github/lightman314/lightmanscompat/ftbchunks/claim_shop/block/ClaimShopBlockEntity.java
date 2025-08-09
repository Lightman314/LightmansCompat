package io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.block;

import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.ClaimShopData;
import io.github.lightman314.lightmanscompat.ftbchunks.core.FTBChunksBlockEntities;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.api.traders.blockentity.TraderBlockEntity;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ClaimShopBlockEntity extends TraderBlockEntity<ClaimShopData> {

    public ClaimShopBlockEntity(BlockPos pos, BlockState state) { this(FTBChunksBlockEntities.CLAIM_SHOP.get(), pos, state); }
    protected ClaimShopBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) { super(type,pos,state); }

    @Override
    protected ClaimShopData buildNewTrader() {
        assert this.level != null;
        return new ClaimShopData(this.level,this.worldPosition);
    }

    @Override
    @Nullable
    protected ClaimShopData castOrNullify(@Nonnull TraderData traderData) {
        if(traderData instanceof ClaimShopData csd)
            return csd;
        return null;
    }

}