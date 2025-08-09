package io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.block;

import io.github.lightman314.lightmanscompat.ftbchunks.FTBChunksText;
import io.github.lightman314.lightmanscompat.ftbchunks.claim_shop.trader.ClaimShopData;
import io.github.lightman314.lightmanscompat.ftbchunks.core.FTBChunksBlockEntities;
import io.github.lightman314.lightmanscurrency.api.traders.blocks.TraderBlockRotatable;
import io.github.lightman314.lightmanscurrency.common.blocks.variant.IVariantBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Supplier;

public class ClaimShopBlock extends TraderBlockRotatable implements IVariantBlock {

    public ClaimShopBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(ClaimShopState.PROPERTY, ClaimShopState.INACTIVE));
    }

    @Override
    protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ClaimShopState.PROPERTY);
    }

    @Override
    protected BlockEntity makeTrader(BlockPos blockPos, BlockState blockState) { return new ClaimShopBlockEntity(blockPos,blockState); }
    @Override
    protected BlockEntityType<?> traderType() { return FTBChunksBlockEntities.CLAIM_SHOP.get(); }

    @Override
    public boolean canBreak(@Nonnull Player player, @Nonnull LevelAccessor level, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        if(this.getBlockEntity(state,level,pos) instanceof ClaimShopBlockEntity be)
        {
            ClaimShopData shop = be.getTraderData();
            //Cannot be broken while rented
            if(shop != null && shop.isCurrentlyRented())
                return false;
        }
        return super.canBreak(player, level, pos, state);
    }

    @Override
    protected Supplier<List<Component>> getItemTooltips() { return FTBChunksText.TOOLTIP_CLAIM_SHOP.asTooltip(); }

    //Model Variant Overrides
    @Override
    public int requiredModels() { return ClaimShopState.values().length; }
    @Override
    public int getModelIndex(BlockState state) { return state.getValue(ClaimShopState.PROPERTY).ordinal(); }

}