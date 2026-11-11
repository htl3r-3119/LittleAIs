package net.little.ais.ai.tensor;

import net.little.ais.ai.math.Matrix;

public class MulTensor extends Tensor {

    public MulTensor(Tensor a, Tensor b) {
        super(a.MATRIX.mul(b.MATRIX));
        this.PARENTS = new Tensor[]{a, b};
    }

    @Override
    public void backward(Matrix grad) {
        super.backward(grad);
        this.PARENTS[0].backward(grad.mul(this.PARENTS[1].MATRIX));
        this.PARENTS[1].backward(grad.mul(this.PARENTS[0].MATRIX));
    }
}
