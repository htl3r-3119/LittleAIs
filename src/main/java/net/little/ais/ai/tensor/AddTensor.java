package net.little.ais.ai.tensor;

import net.little.ais.ai.math.Matrix;

public class AddTensor extends Tensor {

    public AddTensor(Tensor a, Tensor b) {
        super(a.MATRIX.add(b.MATRIX));
        this.PARENTS = new Tensor[]{a, b};
    }

    @Override
    public void backward(Matrix grad) {
        super.backward(grad);
        this.PARENTS[0].backward(grad);
        this.PARENTS[1].backward(grad);
    }
}
