package net.little.ais.ai.tensor;

import net.little.ais.ai.math.Matrix;

public class SoftmaxTensor extends Tensor {

    public SoftmaxTensor(Tensor m) {
        super(m.MATRIX.clone());
        this.MATRIX.iterateSelf(Math::exp);
        double sum = this.MATRIX.sum() + 1e-10;
        this.MATRIX.iterateSelf(x -> x / sum);
        this.PARENTS = new Tensor[] {
                m
        };
    }

    @Override
    public void backward(Matrix grad) {
        super.backward(grad);
        this.PARENTS[0].backward(grad);
    }
}
