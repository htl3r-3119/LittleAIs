package net.little.ais.block.entity;

import net.little.ais.ai.math.Matrix;
import net.little.ais.ai.model.DataSet;
import net.little.ais.ai.model.RedstoneConverterModel;
import net.little.ais.block.RedstoneFunctionConverterBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

public class RedstoneFunctionConverterBlockEntity extends BlockEntity {

    private static final int MAX_TRAINING_TIME = 20*30;

    private int signal = 0;
    private final RedstoneConverterModel MODEL;
    private int left = 0, down = 0, right = 0;
    private int ticksTraining = 0;

    public RedstoneFunctionConverterBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.REDSTONE_FUNCTION_CONVERTER_BLOCK_ENTITY_BLOCK_ENTITY_TYPE, worldPosition, blockState);
        this.MODEL = new RedstoneConverterModel();
        this.MODEL.dataSet.add(0,0,0,15);
        this.MODEL.dataSet.add(0,0,15,0);
        this.MODEL.dataSet.add(0,15,0,0);
        this.MODEL.dataSet.add(15,0,0,0);
        this.MODEL.dataSet.add(15,15,15,15);
    }

    public DataSet getData() {
        return this.MODEL.dataSet;
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, RedstoneFunctionConverterBlockEntity eR) {
        // eR.signal = (eR.signal+1)%16;
        // System.out.println("TICK: " + level + ", " + eR.signal);
        // eR.setChanged();
        if (blockState.getValue(RedstoneFunctionConverterBlock.IS_TRAINING) && !level.isClientSide()) {
            double train = eR.MODEL.train();
            eR.ticksTraining = eR.ticksTraining + 1;
            if (eR.ticksTraining >= MAX_TRAINING_TIME) {
                eR.ticksTraining = 0;
                level.setBlock(blockPos, blockState.setValue(RedstoneFunctionConverterBlock.IS_TRAINING, false), Block.UPDATE_ALL);
            }
            eR.signal = eR.MODEL.predict(eR.left,eR.down,eR.right);
            eR.setChanged();
        }
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public int signal() {
        return this.signal;
    }

    @Override
    public void setChanged() {
        super.setChanged();

        if (level == null) return;

        BlockState blockState = getBlockState();
        level.sendBlockUpdated(worldPosition, blockState, blockState, Block.UPDATE_ALL);
        level.updateNeighborsAt(worldPosition, blockState.getBlock());
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        output.putInt("signal", this.signal);
        super.saveAdditional(output);
    }

    private void saveMatrix(String name, Matrix m, ValueOutput output) {

    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.signal = input.getIntOr("signal", 0);
    }

    private void loadMatrix(String name, Matrix m, ValueInput input) {

    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, T t) {
        if (t instanceof RedstoneFunctionConverterBlockEntity rE) tick(level,blockPos,blockState,rE);
    }

    public void updateInputs(int left, int down, int right) {
        if (this.left != left || this.down != down || this.right != right) {
            this.signal = this.MODEL.predict(left, down, right);
            this.left = left;
            this.down = down;
            this.right = right;
            BlockState blockState1 = getBlockState().setValue(RedstoneFunctionConverterBlock.POWERED_LEFT, this.left() > 0)
                    .setValue(RedstoneFunctionConverterBlock.POWERED_DOWN, this.down() > 0)
                    .setValue(RedstoneFunctionConverterBlock.POWERED_RIGHT, this.right() > 0)
                    .setValue(RedstoneFunctionConverterBlock.POWERED_UP, this.signal() > 0);
            if (level != null) level.setBlock(getBlockPos(), blockState1, Block.UPDATE_ALL);
            this.setChanged();
        }
    }

    public void readInputSignal(int[] arr) {
        arr[0] = this.left;
        arr[1] = this.down;
        arr[2] = this.right;
    }

    public int left() {
        return left;
    }

    public int down() {
        return down;
    }

    public int right() {
        return right;
    }
}
