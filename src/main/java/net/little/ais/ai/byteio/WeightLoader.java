package net.little.ais.ai.byteio;

import net.little.ais.LittleAIs;

import java.io.*;

public final class WeightLoader {

    public static double[] loadWeight(String path) throws IOException {
        try (InputStream is = LittleAIs.class.getClassLoader().getResourceAsStream(path)) {

            if (is == null) {
                throw new FileNotFoundException(path);
            }

            ByteArrayOutputStream bOut = new ByteArrayOutputStream();

            is.transferTo(bOut);

            byte[] bytes = bOut.toByteArray();
            double[] arr = new double[bytes.length/Float.BYTES];
            int n=0;
            for (int i=0; i<bytes.length; i+=Float.BYTES) {
                arr[n++] = readFloat(bytes, i);
            }
            return arr;
        }
    }

    private static float readFloat(byte[] arr, int start) {
        int i = arr[start+Float.BYTES-1];
        for (int j = Float.BYTES-1; j > 0; j--) {
            i <<= 8;
            i |= arr[start+j]&255;
        }
        return Float.intBitsToFloat(i);
    }

}
