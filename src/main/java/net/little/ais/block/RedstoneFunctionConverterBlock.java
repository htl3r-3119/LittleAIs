package net.little.ais.block;

import net.little.ais.block.entity.RedstoneFunctionConverterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

public class RedstoneFunctionConverterBlock extends DiodeBlock implements EntityBlock {

    public static final BooleanProperty POWERED_LEFT = BooleanProperty.create("powered_left");
    public static final BooleanProperty POWERED_DOWN = BooleanProperty.create("powered_down");
    public static final BooleanProperty POWERED_RIGHT = BooleanProperty.create("powered_right");
    public static final BooleanProperty POWERED_UP = BooleanProperty.create("powered_up");
    public static final BooleanProperty IS_TRAINING = BooleanProperty.create("is_training");

    protected RedstoneFunctionConverterBlock(Properties properties) {
        super(properties);

        registerDefaultState(getStateDefinition().any()
                .setValue(POWERED_LEFT, false)
                .setValue(POWERED_DOWN, false)
                .setValue(POWERED_RIGHT, false)
                .setValue(POWERED_UP, false)
                .setValue(IS_TRAINING, false)
        );
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new RedstoneFunctionConverterBlockEntity(worldPosition, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return RedstoneFunctionConverterBlockEntity::tick;
    }

    @Override
    protected boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        if (state.getValue(FACING) == direction) {
            if (!(level.getBlockEntity(pos) instanceof RedstoneFunctionConverterBlockEntity rE)) {
                return 0;
            }
            return rE.signal();
        }
        return 0;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!player.isShiftKeyDown()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof RedstoneFunctionConverterBlockEntity eR) {
                BlockState blockState = state.setValue(IS_TRAINING, !state.getValue(IS_TRAINING));
                level.setBlock(pos, blockState, Block.UPDATE_ALL);
                player.sendOverlayMessage(Component.literal("Converter is Learning: " + blockState.getValue(IS_TRAINING)));
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.FAIL;
        }
        return super.useWithoutItem(state,level,pos,player,hitResult);
    }

    @Override
    protected int getInputSignal(Level level, BlockPos pos, BlockState state) {
        int left = readSignalFromLeft(level, pos, state);
        int down = readSignalFromDown(level, pos, state);
        int right = readSignalFromRight(level, pos, state);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof RedstoneFunctionConverterBlockEntity eR) {
            if (!level.isClientSide()) eR.updateInputs(left, down, right);
            else eR.setChanged();
        }
        return Math.max(left, Math.max(down, right));
    }

    private int readSignalFromDown(Level level, BlockPos pos, BlockState state) {
        Direction value = state.getValue(FACING);
        BlockPos target = pos.relative(value);
        return level.getSignal(target, value);
    }
    private int readSignalFromLeft(Level level, BlockPos pos, BlockState state) {
        Direction value = state.getValue(FACING).getClockWise();
        BlockPos target = pos.relative(value);
        return level.getSignal(target, value);
    }
    private int readSignalFromRight(Level level, BlockPos pos, BlockState state) {
        Direction value = state.getValue(FACING).getCounterClockWise();
        BlockPos target = pos.relative(value);
        return level.getSignal(target, value);
    }

    @Override
    protected int getDelay(BlockState state) {
        return 0;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.tick(state, level, pos, random);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED, POWERED_LEFT, POWERED_DOWN, POWERED_RIGHT, POWERED_UP, IS_TRAINING);
    }

}
