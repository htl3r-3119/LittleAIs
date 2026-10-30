package net.little.ais.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.little.ais.ai.model.DataSet;

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
                    System.out.println("RECEIVED: " + data);
                }
        );
    }

}
