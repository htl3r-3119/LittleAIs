package net.little.ais.ai.opt;

import net.little.ais.ai.math.Functions;
import net.little.ais.ai.math.Matrix;
import net.little.ais.ai.tensor.Tensor;

import java.util.List;

public class SGD extends Optimizer {

    public SGD(List<Tensor> params) {
        super(params);
    }

    @Override
    public void step(double lr) {
        this.params.forEach(tensor -> {
            Matrix grad = tensor.GRAD.clone().iterateSelf(aDouble -> aDouble*lr);
            tensor.MATRIX.iterateSelf(Functions::SUB,grad);
        });
    }

}
