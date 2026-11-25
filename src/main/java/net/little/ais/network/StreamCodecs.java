package net.little.ais.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.Arrays;

public class StreamCodecs {

    public static final StreamCodec<FriendlyByteBuf, double[]> DATA_CODEC = StreamCodec.of(
            (output, value) -> output.writeLongArray(Arrays.stream(value).mapToLong(Double::doubleToLongBits).toArray()),
            input -> Arrays.stream(input.readLongArray()).mapToDouble(Double::longBitsToDouble).toArray()
    );

}
