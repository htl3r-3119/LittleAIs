package net.little.ais.block.entity;

import com.ibm.icu.lang.UCharacter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

public class RedstoneFunctionConverterBlockEntity extends BlockEntity {

    private int signal = 0;

    public RedstoneFunctionConverterBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.REDSTONE_FUNCTION_CONVERTER_BLOCK_ENTITY_BLOCK_ENTITY_TYPE, worldPosition, blockState);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, RedstoneFunctionConverterBlockEntity redstoneFunctionConverterBlockEntity) {
        redstoneFunctionConverterBlockEntity.signal = (redstoneFunctionConverterBlockEntity.signal+1)%16;
        // System.out.println("TICK: " + level + ", " + redstoneFunctionConverterBlockEntity.signal);
        redstoneFunctionConverterBlockEntity.setChanged();
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

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.signal = input.getIntOr("signal", 0);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, T t) {
        if (t instanceof RedstoneFunctionConverterBlockEntity rE) tick(level,blockPos,blockState,rE);
    }
}
