package net.little.ais.ai.tensor;

import net.little.ais.ai.math.Matrix;

public class SubTensor extends Tensor {

    public SubTensor(Tensor a, Tensor b) {
        super(a.MATRIX.sub(b.MATRIX));
        this.PARENTS = new Tensor[]{a, b};
    }

    @Override
    public void backward(Matrix grad) {
        this.push(grad);
        this.PARENTS[0].backward(grad.iterateSelf(aDouble -> aDouble*-1));
        this.PARENTS[1].backward(grad);
    }
}
