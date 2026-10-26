package net.little.ais.ai.layer;

import net.little.ais.ai.tensor.SoftmaxTensor;
import net.little.ais.ai.tensor.Tensor;

import java.util.List;

public class Softmax implements Layer {

    @Override
    public Tensor forward(Tensor input) {
        return new SoftmaxTensor(input);
    }

    @Override
    public List<Tensor> getParams() {
        return List.of();
    }

}
