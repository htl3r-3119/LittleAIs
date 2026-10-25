package net.little.ais.ai.layer;

import net.little.ais.ai.math.Matrix;
import net.little.ais.ai.tensor.Tensor;

import java.util.List;

public class Linear implements Layer {

    Tensor W;
    Tensor B;

    public Linear(int i, int o) {
        this.W = new Tensor(new Matrix(i,o));
        this.B = new Tensor(new Matrix(1,o));
    }

    @Override
    public Tensor forward(Tensor input) {
        return input.matmul(W).add(B);
    }

    @Override
    public List<Tensor> getParams() {
        return List.of(this.W, this.B);
    }
}
