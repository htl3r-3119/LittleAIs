package net.little.ais.ai.layer;

import net.little.ais.ai.tensor.Tensor;

import java.util.ArrayList;
import java.util.List;

public class Sequential implements Layer {

    List<Layer> layers;

    public Sequential(List<Layer> layers) {
        this.layers = new ArrayList<>(layers);
    }

    @Override
    public Tensor forward(Tensor input) {
        for (Layer l : this.layers) {
            input = l.forward(input);
        }
        return input;
    }

    @Override
    public List<Tensor> getParams() {
        return this.layers.stream().map(Layer::getParams).flatMap(List::stream).toList();
    }
}
