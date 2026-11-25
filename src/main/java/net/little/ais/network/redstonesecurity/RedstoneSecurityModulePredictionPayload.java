package net.little.ais.network.redstonesecurity;

import net.little.ais.LittleAIs;
import net.little.ais.network.StreamCodecs;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record RedstoneSecurityModulePredictionPayload(BlockPos pos, double[] data, boolean test) implements CustomPacketPayload {

    public static final StreamCodec<FriendlyByteBuf, RedstoneSecurityModulePredictionPayload> CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, RedstoneSecurityModulePredictionPayload::pos,
            StreamCodecs.DATA_CODEC, RedstoneSecurityModulePredictionPayload::data,
            ByteBufCodecs.BOOL, RedstoneSecurityModulePredictionPayload::test,
            RedstoneSecurityModulePredictionPayload::new
    );

    public static final Type<RedstoneSecurityModulePredictionPayload> TYPE = new Type<>(LittleAIs.id("redsecmodpred"));

    @Override
    public Type<? extends RedstoneSecurityModulePredictionPayload> type() {
        return TYPE;
    }

}
