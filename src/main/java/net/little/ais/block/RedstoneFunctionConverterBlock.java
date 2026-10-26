package net.little.ais.block;

import net.little.ais.block.entity.ModBlockEntities;
import net.little.ais.block.entity.RedstoneFunctionConverterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class RedstoneFunctionConverterBlock extends BaseEntityBlock {

    protected RedstoneFunctionConverterBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new RedstoneFunctionConverterBlockEntity(worldPosition, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return createTickerHelper(
                type,
                ModBlockEntities.REDSTONE_FUNCTION_CONVERTER_BLOCK_ENTITY_BLOCK_ENTITY_TYPE,
                RedstoneFunctionConverterBlockEntity::tick
        );
    }

    @Override
    protected boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    protected int ownSignal(BlockState state, BlockGetter level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof RedstoneFunctionConverterBlockEntity rE)) {
            return 0;
        }
        System.out.println("GET SIGNAL");
        return rE.signal();
    }

    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        if (!(level.getBlockEntity(pos) instanceof RedstoneFunctionConverterBlockEntity rE)) {
            return 0;
        }
        return rE.signal();
    }
}
