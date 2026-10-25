package net.little.ais.ai.math;

public class BatchMatrix extends Matrix {

    int b;

    public BatchMatrix(int b, int r, int c) {
        super(r*b, c);
        this.b = b;
        this.r /= b;
    }

}
