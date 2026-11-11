package net.little.ais.block.entity;

import net.little.ais.block.ModBlocks;
import net.little.ais.block.RedstoneSecurityModuleBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class RedstoneSecurityBlockEntity extends BlockEntity {

    private UUID owner;
    private int required;
    private int signalStrength = 1;

    public RedstoneSecurityBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.REDSTONE_SECURITY_BLOCK_ENTITY_TYPE, worldPosition, blockState);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        output.putString("owner", this.owner.toString());
        output.putInt("required", this.required);
        output.putInt("signalStrength", this.signalStrength);
        super.saveAdditional(output);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        Optional<String> string = input.getString("owner");
        string.ifPresent(s -> this.owner = UUID.fromString(s));
        this.required = input.getIntOr("required", -1);
        this.signalStrength = input.getIntOr("signalStrength", 1);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void setChanged() {
        super.setChanged();

        if (this.level == null) return;

        BlockState blockState = getBlockState();
        this.level.sendBlockUpdated(this.worldPosition, blockState, blockState, Block.UPDATE_ALL);
        this.level.updateNeighborsAt(this.worldPosition, blockState.getBlock());
    }

    public int required() {
        return this.required;
    }

    public void setRequired(int r) {
        this.required = Math.min(9, Math.max(0,r));
    }

    public void setOwner(UUID uuid) {
        this.owner = uuid;
    }

    public boolean isOwnedBy(UUID owner) {
        return this.owner.equals(owner);
    }

    public void validate(int pred) {
        if (this.level == null || this.level.isClientSide()) return;

        BlockPos blockPos = this.getBlockPos();
        BlockState blockState = this.level.getBlockState(blockPos);
        this.signalStrength = pred == this.required ? 15 : 1;
        this.level.setBlock(blockPos, blockState.setValue(RedstoneSecurityModuleBlock.POWERED, true), Block.UPDATE_ALL);
        setChanged();
        this.level.scheduleTick(blockPos, ModBlocks.REDSTONE_SECURITY_MODULE_BLOCK, RedstoneSecurityModuleBlock.TICK_DELAY);
    }

    public int getSignalStrength() {
        return this.signalStrength;
    }

    public UUID getOwner() {
        return this.owner;
    }

    public void setSignalStrength(int i) {
        this.signalStrength = i;
    }

}
