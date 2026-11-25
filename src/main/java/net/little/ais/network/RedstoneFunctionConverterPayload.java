package net.little.ais.network;

import net.little.ais.LittleAIs;
import net.little.ais.ai.model.DataSet;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record RedstoneFunctionConverterPayload(BlockPos pos, DataSet dataSet) implements CustomPacketPayload {

    public static final Type<RedstoneFunctionConverterPayload> TYPE = new Type<>(LittleAIs.id("redstoneconverter"));

    public static final StreamCodec<RegistryFriendlyByteBuf, RedstoneFunctionConverterPayload> CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, RedstoneFunctionConverterPayload::pos,
            DataSet.STREAM_CODEC, RedstoneFunctionConverterPayload::dataSet,
            RedstoneFunctionConverterPayload::new
    );

    @Override
    public Type<RedstoneFunctionConverterPayload> type() {
        return TYPE;
    }

}
