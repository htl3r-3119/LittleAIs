package net.little.ais.network;

import net.little.ais.LittleAIs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record DrawingGameOpenSettingsPayload() implements CustomPacketPayload {

    private static final DrawingGameOpenSettingsPayload INSTANCE = new DrawingGameOpenSettingsPayload();

    public static final StreamCodec<RegistryFriendlyByteBuf, DrawingGameOpenSettingsPayload> CODEC = StreamCodec.unit(INSTANCE);
    public static Type<DrawingGameOpenSettingsPayload> TYPE = new Type<>(LittleAIs.id(
            "drawinggamesettings"
    ));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}
