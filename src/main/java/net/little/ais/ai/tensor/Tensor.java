package net.little.ais.ai.tensor;

import net.little.ais.ai.math.Functions;
import net.little.ais.ai.math.Matrix;

public class Tensor {

    public Matrix MATRIX;
    Tensor[] PARENTS;
    public Matrix GRAD;

    public Tensor(Matrix m) {
        this.MATRIX = m;
        GRAD = m.clone().iterateSelf(_ -> 0.);
    }

    public Tensor add(Tensor t) {
        return new AddTensor(this,t);
    }

    public Tensor sub(Tensor t) {
        return new SubTensor(this,t);
    }

    public Tensor mul(Tensor t) {
        return new MulTensor(this,t);
    }

    public Tensor div(Tensor t) {
        return new DivTensor(this,t);
    }

    public void push(Matrix grad) {
        this.GRAD.iterateSelf(Functions::ADD, grad);
    }

    public void backward(Matrix grad) {
        this.push(grad);
    }

    public Tensor matmul(Tensor o) {
        return new MatMulTensor(this, o);
    }

    public void zeroGrad() {
        this.GRAD.iterateSelf(_ -> 0.);
    }
}
