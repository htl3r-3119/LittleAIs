package net.little.ais.ai.loss;

import net.little.ais.ai.tensor.CrossEntropyTensor;
import net.little.ais.ai.tensor.Tensor;

public class CrossEntropy implements Loss {

    @Override
    public Tensor forward(Tensor output, Tensor target) {
        return new CrossEntropyTensor(output, target);
    }

}
