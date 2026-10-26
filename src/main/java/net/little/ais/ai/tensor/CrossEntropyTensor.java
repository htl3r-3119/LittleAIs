package net.little.ais.ai.tensor;

import net.little.ais.ai.math.Matrix;

public class CrossEntropyTensor extends Tensor {

    public CrossEntropyTensor(Tensor logits, Tensor labels) {
        super(
                labels.MATRIX.mul(logits.MATRIX.clone().iterateSelf(Math::log))
        );
        this.PARENTS = new Tensor[]{
                logits,
                labels
        };
    }

    @Override
    public void backward(Matrix grad) {
        super.backward(grad);
        this.PARENTS[0].backward(grad.mul(this.PARENTS[0].sub(this.PARENTS[1]).MATRIX));
    }
}
