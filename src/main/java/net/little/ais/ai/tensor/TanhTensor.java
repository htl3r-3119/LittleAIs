package net.little.ais.ai.tensor;

import net.little.ais.ai.math.Functions;
import net.little.ais.ai.math.Matrix;

public class TanhTensor extends Tensor {

    public TanhTensor(Tensor t) {
        super(t.MATRIX.clone().iterateSelf(Functions::TANH));
        this.PARENTS = new Tensor[]{t};
    }

    @Override
    public void backward(Matrix grad) {
        super.backward(grad);
        this.PARENTS[0].backward(grad.mul(this.MATRIX.clone().iterateSelf(Functions::TANHD)));
    }
}
