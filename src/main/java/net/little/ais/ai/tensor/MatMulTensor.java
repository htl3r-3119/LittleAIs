package net.little.ais.ai.tensor;

import net.little.ais.ai.math.Matrix;

public class MatMulTensor extends Tensor {

    public MatMulTensor(Tensor a, Tensor b) {
        super(a.MATRIX.matmul(b.MATRIX));
        this.PARENTS = new Tensor[]{a,b};
    }

    @Override
    public void backward(Matrix grad) {
        super.backward(grad);
        /*
        z(a,c) = x(a,b) * y(b,c)
        z'(x) = y(b,c)
        z'(y) = x(a,b)
        # x
        x(a,b) = (a,c) x (c,b)
        # y
        y(b,c) = (b,a) x (a,c)
        */
        this.PARENTS[0].backward(grad.matmul(this.PARENTS[1].MATRIX.transpose()));
        this.PARENTS[1].backward(this.PARENTS[0].MATRIX.transpose().matmul(grad));
    }
}
