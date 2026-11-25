package net.little.ais.block;

import net.little.ais.block.entity.RedstoneSecurityBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jspecify.annotations.Nullable;

public class RedstoneSecurityModuleBlock extends HorizontalDirectionalBlock implements EntityBlock {

    public static final BooleanProperty POWERED;
    public static final int TICK_DELAY = 20;

    protected RedstoneSecurityModuleBlock(Properties properties) {
        super(properties);

        registerDefaultState(
                getStateDefinition().any()
                .setValue(POWERED, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED);
    }

    @Override
    protected boolean isSignalSource(BlockState state) {
        return true;
    }

    static {
        POWERED = BlockStateProperties.POWERED;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new RedstoneSecurityBlockEntity(worldPosition,blockState);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity by, ItemStack itemStack) {
        super.setPlacedBy(level, pos, state, by, itemStack);
        if (by instanceof Player p) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof RedstoneSecurityBlockEntity rS) {
                rS.setOwner(p.getUUID());
            }
        }
    }

    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected boolean shouldRedstoneWireConnectTo(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        if (direction == null) return false;
        return state.getValue(FACING) == direction;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.tick(state, level, pos, random);
        BlockState blockState = level.getBlockState(pos);
        level.setBlock(pos, blockState.setValue(POWERED, false), Block.UPDATE_ALL);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof RedstoneSecurityBlockEntity rS)) return;
        rS.setSignalStrength(1);
    }

    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        if (state.getValue(POWERED) && state.getValue(FACING) == direction) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (!(blockEntity instanceof RedstoneSecurityBlockEntity rS)) return 0;
            return rS.getSignalStrength();
        }
        return 0;
    }

}
