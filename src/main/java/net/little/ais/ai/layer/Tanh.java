package net.little.ais.ai.layer;

import net.little.ais.ai.tensor.TanhTensor;
import net.little.ais.ai.tensor.Tensor;

import java.util.List;

public class Tanh implements Layer {

    @Override
    public Tensor forward(Tensor input) {
        return new TanhTensor(input);
    }

    @Override
    public List<Tensor> getParams() {
        return List.of();
    }

}
