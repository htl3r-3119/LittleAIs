package net.little.ais.ai.model;

import net.little.ais.ai.byteio.WeightLoader;
import net.little.ais.ai.layer.*;
import net.little.ais.ai.math.Functions;
import net.little.ais.ai.math.Matrix;
import net.little.ais.ai.tensor.Tensor;

import java.io.IOException;
import java.util.List;

public class RedstoneSecurityModel {

    Layer model;

    public RedstoneSecurityModel() {
        Conv2d conv2d = new Conv2d(32, 3,3, 28,28,1,1);
        Linear linear = new Linear(5408, 10);

        this.model = new Sequential(List.of(
                conv2d,
                new Relu(),
                new MaxPool2d(2),
                new Flatten(),
                linear
        ));

        try {
            double[] floats = WeightLoader.loadWeight("assets/little_ais/data/weight_0");
            conv2d.getParams().getFirst().MATRIX.fill(floats);
            double[] floats1 = WeightLoader.loadWeight("assets/little_ais/data/weight_1");
            conv2d.getParams().getLast().MATRIX.fill(floats1);
            double[] floats2 = WeightLoader.loadWeight("assets/little_ais/data/weight_2");
            linear.getParams().getFirst().MATRIX.fill(floats2);
            double[] floats3 = WeightLoader.loadWeight("assets/little_ais/data/weight_3");
            linear.getParams().getLast().MATRIX.fill(floats3);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public int predict(Tensor input) {
        Tensor forward = this.model.forward(input);
        Matrix softmax = Functions.softmax(forward.MATRIX);
        return softmax.argMax();
    }

}
