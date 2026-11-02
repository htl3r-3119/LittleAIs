package net.little.ais.ai.byteio;

import net.little.ais.LittleAIs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class WeightLoader {

    public static void loadWeight(String path) throws IOException {
        try (InputStream is = LittleAIs.class.getResourceAsStream(path)) {

            if (is == null) throw new FileNotFoundException(path);

            byte[] bytes = is.readAllBytes();

            for (int i=0; i<bytes.length; i+=Float.BYTES) {
                System.out.println(readFloat(bytes,i));
            }
        }
    }

    private static float readFloat(byte[] arr, int start) {
        int i = arr[start];
        for (int j = start+1; j < start+Float.BYTES; j++) {
            i <<= 8;
            i |= arr[j];
        }
        return Float.intBitsToFloat(i);
    }

}
