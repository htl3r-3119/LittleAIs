package net.little.ais.network.redstonesecurity;

import net.little.ais.LittleAIs;
import net.little.ais.network.StreamCodecs;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record RedstoneSecurityModulePayload(BlockPos pos, String owner, double[] required) implements CustomPacketPayload {

    public static final Type<RedstoneSecurityModulePayload> TYPE = new Type<>(LittleAIs.id("redstonesecuritymodule"));

    public static final StreamCodec<RegistryFriendlyByteBuf, RedstoneSecurityModulePayload> CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, RedstoneSecurityModulePayload::pos,
            ByteBufCodecs.STRING_UTF8, RedstoneSecurityModulePayload::owner,
            StreamCodecs.DATA_CODEC, RedstoneSecurityModulePayload::required,
            RedstoneSecurityModulePayload::new
    );

    @Override
    public Type<RedstoneSecurityModulePayload> type() {
        return TYPE;
    }
}
