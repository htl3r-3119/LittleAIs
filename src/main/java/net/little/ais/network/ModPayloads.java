package net.little.ais.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.little.ais.ai.math.BatchMatrix;
import net.little.ais.ai.model.DataSet;
import net.little.ais.ai.model.StaticModel;
import net.little.ais.ai.tensor.Tensor;
import net.little.ais.block.entity.RedstoneSecurityBlockEntity;
import net.little.ais.network.redstonefunctionconverter.RedstoneFunctionConverterPayload;
import net.little.ais.network.redstonesecurity.RedstoneSecurityModuleCheckPayload;
import net.little.ais.network.redstonesecurity.RedstoneSecurityModulePayload;
import net.little.ais.network.redstonesecurity.RedstoneSecurityModulePredictionPayload;
import net.little.ais.network.redstonesecurity.RedstoneSecurityModulePredictionResultPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ModPayloads {

    public static void initialize() {
        PayloadTypeRegistry.serverboundPlay().register(
                RedstoneFunctionConverterPayload.TYPE,
                RedstoneFunctionConverterPayload.CODEC
        );

        PayloadTypeRegistry.serverboundPlay().register(
                RedstoneSecurityModulePayload.TYPE,
                RedstoneSecurityModulePayload.CODEC
        );

        PayloadTypeRegistry.serverboundPlay().register(
                RedstoneSecurityModuleCheckPayload.TYPE,
                RedstoneSecurityModuleCheckPayload.CODEC
        );

        PayloadTypeRegistry.clientboundPlay().register(
                RedstoneSecurityModulePredictionResultPayload.TYPE,
                RedstoneSecurityModulePredictionResultPayload.CODEC
        );

        PayloadTypeRegistry.serverboundPlay().register(
                RedstoneSecurityModulePredictionPayload.TYPE,
                RedstoneSecurityModulePredictionPayload.CODEC
        );

        ServerPlayNetworking.registerGlobalReceiver(
                RedstoneSecurityModulePredictionPayload.TYPE,
                (payload, context) -> {
                    BatchMatrix batchMatrix = new BatchMatrix(28, 28, 1);
                    batchMatrix.fill(payload.data());
                    int predict = StaticModel.REDSTONE_SECURITY_MODEL().predict(new Tensor(batchMatrix));
                    if (!payload.test()) {
                        ServerLevel level = context.player().level();
                        BlockEntity blockEntity = level.getBlockEntity(payload.pos());
                        if (blockEntity instanceof RedstoneSecurityBlockEntity ers) {
                            ers.validate(predict);
                        }
                    }
                    ServerPlayNetworking.send(
                            context.player(),
                            new RedstoneSecurityModulePredictionResultPayload(predict)
                    );
                }
        );

        ServerPlayNetworking.registerGlobalReceiver(
                RedstoneFunctionConverterPayload.TYPE,
                (payload, context) -> {
                    DataSet data = payload.dataSet();
                    System.out.println("RECEIVED: " + data);
                }
        );

        ServerPlayNetworking.registerGlobalReceiver(
                RedstoneSecurityModulePayload.TYPE,
                (payload, context) -> {
                    ServerPlayer player = context.player();
                    ServerLevel level = player.level();
                    BlockEntity blockEntity = level.getBlockEntity(payload.pos());
                    if (!(blockEntity instanceof RedstoneSecurityBlockEntity rS)) return;
                    BatchMatrix batchMatrix = new BatchMatrix(28, 28, 1);
                    batchMatrix.fill(payload.required());
                    int predict = StaticModel.REDSTONE_SECURITY_MODEL().predict(new Tensor(batchMatrix));
                    rS.setRequired(predict);
                    rS.setChanged();
                }
        );

        ServerPlayNetworking.registerGlobalReceiver(
                RedstoneSecurityModuleCheckPayload.TYPE,
                (payload, context) -> {
                    ServerLevel level = context.player().level();
                    BlockEntity blockEntity = level.getBlockEntity(payload.pos());
                    if (!(blockEntity instanceof RedstoneSecurityBlockEntity eR)) return;
                    eR.validate(payload.prediction());
                }
        );

    }

}
