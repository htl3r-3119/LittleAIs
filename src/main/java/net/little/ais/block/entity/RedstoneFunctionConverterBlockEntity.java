package net.little.ais.block.entity;

import net.little.ais.ai.math.Matrix;
import net.little.ais.ai.model.DataSet;
import net.little.ais.ai.model.RedstoneConverterModel;
import net.little.ais.ai.tensor.Tensor;
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

import java.util.List;
import java.util.Optional;

public class RedstoneFunctionConverterBlockEntity extends BlockEntity {

    private static final int MAX_TRAINING_TIME = 20*30; // 20 ticks * 30 => 30 seconds of training

    private int signal = 0;
    private final RedstoneConverterModel MODEL;
    private int left = 0, down = 0, right = 0;
    private int ticksTraining = 0;

    public RedstoneFunctionConverterBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.REDSTONE_FUNCTION_CONVERTER_BLOCK_ENTITY_BLOCK_ENTITY_TYPE, worldPosition, blockState);
        this.MODEL = new RedstoneConverterModel();
        this.MODEL.dataSet.add(0,0,0,0);
    }

    public DataSet getData() {
        return this.MODEL.dataSet;
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, RedstoneFunctionConverterBlockEntity eR) {
        if (level.isClientSide()) return;
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
        saveDataSet(output);
        List<Tensor> parameters = this.MODEL.parameters();
        int i=0;
        for (Tensor t : parameters) {
            saveMatrix((i++%2==0 ? "w_" : "b_") + i, t.MATRIX, output);
        }
        super.saveAdditional(output);
    }

    private void saveDataSet(ValueOutput output) {
        output.store("dataset", DataSet.CODEC, this.MODEL.dataSet);
    }

    private void saveMatrix(String name, Matrix m, ValueOutput output) {
        output.store(name, Matrix.CODEC, m);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        loadDataSet(input);
        List<Tensor> parameters = this.MODEL.parameters();
        int i=0;
        for (Tensor t : parameters) {
            loadMatrix((i++%2==0 ? "w_" : "b_") + i, t.MATRIX, input);
        }
        this.signal = input.getIntOr("signal", 0);
    }

    private void loadDataSet(ValueInput input) {
        input.read("dataset", DataSet.CODEC).ifPresent(entries -> {
            this.MODEL.dataSet.clear();
            this.MODEL.dataSet.fill(entries);
        });
    }

    private void loadMatrix(String name, Matrix m, ValueInput input) {
        input.read(name, Matrix.CODEC).ifPresent(matrix -> m.fill(matrix.getVals()));
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithFullMetadata(registries);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, T t) {
        if (t instanceof RedstoneFunctionConverterBlockEntity rE) tick(level,blockPos,blockState,rE);
    }

    public void updateInputs(int left, int down, int right) {
        if (this.level == null || this.level.isClientSide()) return;
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
