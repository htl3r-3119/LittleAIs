package net.little.ais.ai.layer;

import net.little.ais.ai.tensor.Tensor;

import java.util.List;

public interface Layer {

    Tensor forward(Tensor input);

    List<Tensor> getParams();

}
