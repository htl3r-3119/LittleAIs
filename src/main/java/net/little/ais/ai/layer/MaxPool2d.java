package net.little.ais.ai.layer;

import net.little.ais.ai.math.BatchMatrix;
import net.little.ais.ai.math.Matrix;
import net.little.ais.ai.tensor.Tensor;

import java.util.List;

public class MaxPool2d implements Layer {

    int n;

    public MaxPool2d(int n) {
        this.n = n;
    }

    @Override
    public Tensor forward(Tensor input) {
        Matrix matrix = input.MATRIX;
        BatchMatrix bm;
        if (!(matrix instanceof BatchMatrix)) {
            double temp = Math.sqrt(matrix.r());
            bm = new BatchMatrix((int) Math.ceil(temp), (int) Math.floor(temp), matrix.c());
            bm.fill(matrix);
        } else {
            bm = (BatchMatrix) matrix;
        }
        BatchMatrix res = new BatchMatrix(bm.b()/this.n, bm.r()/this.n, bm.c());
        for (int b = 0; b < res.b(); b++) {
            for (int r = 0; r < res.r(); r++) {
                for (int c = 0; c < res.c(); c++) {
                    double max = -Double.MAX_VALUE;
                    int bOff = b*this.n;
                    int rOff = r*this.n;
                    for (int i = 0; i < this.n; i++) {
                        for (int j = 0; j < this.n; j++) {
                            double e = bm.get(bOff+i,rOff+j, c);
                            if (e > max) max = e;
                        }
                    }
                    res.set(b, r, c, max);
                }
            }
        }
        return new Tensor(res);
    }

    @Override
    public List<Tensor> getParams() {
        return List.of();
    }

}
