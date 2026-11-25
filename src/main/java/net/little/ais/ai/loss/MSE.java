package net.little.ais.ai.loss;

import net.little.ais.ai.tensor.Tensor;

public class MSE implements Loss {

    @Override
    public Tensor forward(Tensor output, Tensor target) {
        return output.sub(target).mul(output.sub(target));
    }

}
