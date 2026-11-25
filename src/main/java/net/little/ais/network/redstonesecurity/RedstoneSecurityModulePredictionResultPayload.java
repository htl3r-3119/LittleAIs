package net.little.ais.network.redstonesecurity;

import net.little.ais.LittleAIs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record RedstoneSecurityModulePredictionResultPayload(int res) implements CustomPacketPayload {

    public static final StreamCodec<FriendlyByteBuf, RedstoneSecurityModulePredictionResultPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, RedstoneSecurityModulePredictionResultPayload::res,
            RedstoneSecurityModulePredictionResultPayload::new
    );

    public static final Type<RedstoneSecurityModulePredictionResultPayload> TYPE = new Type<>(LittleAIs.id("redsecmodpred"));

    @Override
    public Type<? extends RedstoneSecurityModulePredictionResultPayload> type() {
        return TYPE;
    }

}
