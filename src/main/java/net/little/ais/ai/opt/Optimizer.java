package net.little.ais.ai.opt;

import net.little.ais.ai.tensor.Tensor;

import java.util.ArrayList;
import java.util.List;

public abstract class Optimizer {

    List<Tensor> params;

    public Optimizer(List<Tensor> params) {
        this.params = new ArrayList<>(params);
    }

    public void zeroGrad() {
        this.params.forEach(Tensor::zeroGrad);
    }

    public abstract void step(double lr);

}
