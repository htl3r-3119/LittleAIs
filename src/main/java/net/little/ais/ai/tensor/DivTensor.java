package net.little.ais.ai.tensor;

import net.little.ais.ai.math.Matrix;

public class DivTensor extends Tensor {

    public DivTensor(Tensor a, Tensor b) {
        super(a.MATRIX.div(b.MATRIX));
        this.PARENTS = new Tensor[]{a, b};
    }

    @Override
    public void backward(Matrix grad) {
        super.backward(grad);
        this.PARENTS[0].backward(grad.mul(this.PARENTS[1].MATRIX.clone().iterateSelf(x -> 1/x)));
        this.PARENTS[1].backward(grad.mul(this.PARENTS[0].MATRIX));
    }
}
