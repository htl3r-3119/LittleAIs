package net.little.ais.client.block.entity.renderstate;

import net.little.ais.ai.model.DataSet;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;

public class RedstoneFunctionConverterBlockEntityRenderState extends BlockEntityRenderState {

    public int[] key = new int[4];
    public int val;
    public boolean isTraining;

}
