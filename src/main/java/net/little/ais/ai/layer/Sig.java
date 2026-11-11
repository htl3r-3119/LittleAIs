package net.little.ais.ai.layer;

import net.little.ais.ai.tensor.SigTensor;
import net.little.ais.ai.tensor.Tensor;

import java.util.List;

public class Sig implements Layer {

    @Override
    public Tensor forward(Tensor input) {
        return new SigTensor(input);
    }

    @Override
    public List<Tensor> getParams() {
        return List.of();
    }

}
