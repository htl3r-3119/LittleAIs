package net.little.ais.ai.layer;

import net.little.ais.ai.math.BatchMatrix;
import net.little.ais.ai.math.Matrix;
import net.little.ais.ai.tensor.Tensor;

import java.util.List;

public class Flatten implements Layer {

    @Override
    public Tensor forward(Tensor input) {
        Matrix matrix = input.MATRIX;
        Matrix res;
        if (matrix instanceof BatchMatrix bm) {
            res = new BatchMatrix(bm.b(), 1, bm.r()*bm.c());
        } else {
            res = new Matrix(1, matrix.r()*matrix.c());
        }
        res.fill(matrix);
        return new Tensor(res);
    }

    @Override
    public List<Tensor> getParams() {
        return List.of();
    }

}
