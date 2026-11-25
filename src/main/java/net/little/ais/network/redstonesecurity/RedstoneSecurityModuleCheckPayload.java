package net.little.ais.network.redstonesecurity;

import net.little.ais.LittleAIs;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record RedstoneSecurityModuleCheckPayload(BlockPos pos, int prediction) implements CustomPacketPayload {

    public static final Type<RedstoneSecurityModuleCheckPayload> TYPE = new Type<>(LittleAIs.id("redstonesecuritycheck"));
    public static final StreamCodec<FriendlyByteBuf, RedstoneSecurityModuleCheckPayload> CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, RedstoneSecurityModuleCheckPayload::pos,
            ByteBufCodecs.INT, RedstoneSecurityModuleCheckPayload::prediction,
            RedstoneSecurityModuleCheckPayload::new
    );

    @Override
    public Type<RedstoneSecurityModuleCheckPayload> type() {
        return TYPE;
    }

}
