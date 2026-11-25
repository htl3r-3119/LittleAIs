package net.little.ais.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.little.ais.ai.model.DataSet;
import net.little.ais.block.entity.RedstoneFunctionConverterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ModPayloads {

    public static void initialize() {
        PayloadTypeRegistry.serverboundPlay().register(
                RedstoneFunctionConverterPayload.TYPE,
                RedstoneFunctionConverterPayload.CODEC
        );

        ServerPlayNetworking.registerGlobalReceiver(
                RedstoneFunctionConverterPayload.TYPE,
                (payload, context) -> {
                    DataSet data = payload.dataSet();
                    BlockPos pos = payload.pos();
                    ServerLevel level = context.player().level();
                    BlockEntity entity = level.getBlockEntity(pos);
                    if (!(entity instanceof RedstoneFunctionConverterBlockEntity rcbe)) {
                        return;
                    }
                    DataSet prev = rcbe.getData();
                    prev.clear();
                    prev.fill(data);
                    rcbe.setChanged();
                }
        );
    }

}
