package net.little.ais.client.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.little.ais.block.entity.RedstoneFunctionConverterBlockEntity;
import net.little.ais.client.block.entity.renderstate.RedstoneFunctionConverterBlockEntityRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class RedstoneFunctionConverterBlockEntityRenderer implements BlockEntityRenderer<RedstoneFunctionConverterBlockEntity, RedstoneFunctionConverterBlockEntityRenderState> {

    public RedstoneFunctionConverterBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public RedstoneFunctionConverterBlockEntityRenderState createRenderState() {
        return new RedstoneFunctionConverterBlockEntityRenderState();
    }

    @Override
    public void submit(RedstoneFunctionConverterBlockEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0.5,1.,0.5);
        poseStack.popPose();
    }

    @Override
    public void extractRenderState(RedstoneFunctionConverterBlockEntity blockEntity, RedstoneFunctionConverterBlockEntityRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);

        blockEntity.readInputSignal(state.key);
        state.val = blockEntity.signal();
        state.isTraining = blockEntity.shouldTrain;
    }
}
