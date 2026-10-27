package net.little.ais.ai.loss;

import net.little.ais.ai.tensor.Tensor;

public interface Loss {

    Tensor forward(Tensor output, Tensor target);

}
