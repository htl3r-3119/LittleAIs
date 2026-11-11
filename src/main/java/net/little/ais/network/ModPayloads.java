package net.little.ais.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.little.ais.ai.model.DataSet;
import net.little.ais.block.entity.RedstoneSecurityBlockEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

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
                    rS.setOwner(UUID.fromString(payload.owner()));
                    rS.setRequired(payload.required());
                    rS.setChanged();
                }
        );

        ServerPlayNetworking.registerGlobalReceiver(
                RedstoneSecurityModuleCheckPayload.TYPE,
                (payload, context) -> {
                    ServerLevel level = context.player().level();
                    BlockEntity blockEntity = level.getBlockEntity(payload.pos());
                    if (!(blockEntity instanceof RedstoneSecurityBlockEntity eR)) return;
                    eR.setOwner(context.player().getUUID());
                    eR.validate(payload.prediction());
                }
        );

    }

}
