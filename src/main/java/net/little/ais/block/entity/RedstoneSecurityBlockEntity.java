package net.little.ais.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.UUID;

public class RedstoneSecurityBlockEntity extends BlockEntity {

    private UUID owner;
    private int required;

    public RedstoneSecurityBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.REDSTONE_SECURITY_BLOCK_ENTITY_TYPE, worldPosition, blockState);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
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

}
