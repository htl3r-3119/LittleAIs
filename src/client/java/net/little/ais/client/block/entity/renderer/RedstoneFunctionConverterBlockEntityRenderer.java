package net.little.ais.client.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.little.ais.block.RedstoneFunctionConverterBlock;
import net.little.ais.block.entity.RedstoneFunctionConverterBlockEntity;
import net.little.ais.client.block.entity.renderstate.RedstoneFunctionConverterBlockEntityRenderState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.jspecify.annotations.Nullable;

import java.awt.*;

public class RedstoneFunctionConverterBlockEntityRenderer implements BlockEntityRenderer<RedstoneFunctionConverterBlockEntity, RedstoneFunctionConverterBlockEntityRenderState> {

    public RedstoneFunctionConverterBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public RedstoneFunctionConverterBlockEntityRenderState createRenderState() {
        return new RedstoneFunctionConverterBlockEntityRenderState();
    }

    @Override
    public void submit(RedstoneFunctionConverterBlockEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (!state.isTraining) return;
        poseStack.pushPose();
        poseStack.translate(0.5,0.4,0.5);
        poseStack.rotate(Axis.YP.rotationDegrees(-camera.yRot + 180f));
        poseStack.rotate(Axis.XP.rotationDegrees(90));

        poseStack.scale(1/40f, 1/40f, 1/40f);

        String s = "Is training";
        int width = Minecraft.getInstance().font.width(s);

        submitNodeCollector.submitText(
                poseStack,
                -width/2f, -4f,
                Component.literal(s).getVisualOrderText(),
                true,
                Font.DisplayMode.SEE_THROUGH,
                state.lightCoords,
                0xffffffff,
                0,
                Color.BLACK.getRGB()
        );
        poseStack.popPose();
    }

    @Override
    public void extractRenderState(RedstoneFunctionConverterBlockEntity blockEntity, RedstoneFunctionConverterBlockEntityRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.isTraining = blockEntity.getBlockState().getValue(RedstoneFunctionConverterBlock.IS_TRAINING);
    }
}
