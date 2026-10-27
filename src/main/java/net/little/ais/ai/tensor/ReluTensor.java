package net.little.ais.ai.tensor;

import net.little.ais.ai.math.Functions;
import net.little.ais.ai.math.Matrix;

public class ReluTensor extends Tensor {

    public ReluTensor(Tensor t) {
        super(t.MATRIX.clone().iterateSelf(Functions::RELU));
        this.PARENTS = new Tensor[]{t};
    }

    @Override
    public void backward(Matrix grad) {
        super.backward(grad);
        Matrix prev = this.PARENTS[0].MATRIX.clone().iterateSelf(Functions::RELUD);
        this.PARENTS[0].backward(grad.iterateSelf(Functions::MUL, prev));
    }
}
