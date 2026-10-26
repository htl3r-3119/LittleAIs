package net.little.ais.ai.tensor;

import net.little.ais.ai.math.BatchMatrix;
import net.little.ais.ai.math.Functions;
import net.little.ais.ai.math.Matrix;

public class CrossEntropyTensor extends Tensor {

    Tensor softmax;

    private static Matrix crossEntropy(Matrix softmax, Matrix labels) {
        Matrix mul = labels.clone().iterateSelf(x -> -x).mul(softmax.clone().iterateSelf(Math::log));
        if (mul instanceof BatchMatrix bm) {
            return bm.sumPerBatch();
        }
        Matrix m = new Matrix(1,1);
        m.fill(mul.sum());
        return m;
    }

    public CrossEntropyTensor(Tensor logits, Tensor labels) {
        this(Functions.softmax(logits.MATRIX.clone()), logits, labels);
    }

    private CrossEntropyTensor(Matrix softmax, Tensor logits, Tensor labels) {
        super(crossEntropy(softmax, labels.MATRIX));
        this.softmax = new Tensor(softmax);
        this.softmax.PARENTS = new Tensor[]{logits};
        this.PARENTS = new Tensor[]{
                logits,
                labels
        };
    }

    @Override
    public void backward(Matrix grad) {
        super.backward(grad);
        Tensor g = this.softmax.sub(this.PARENTS[1]);
        this.PARENTS[0].backward(grad.mul(g.MATRIX));
    }
}
