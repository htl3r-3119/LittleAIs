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
        Linear linear1 = new Linear(5408, 128);
        Linear linear2 = new Linear(128,10);

        this.model = new Sequential(List.of(
                conv2d,
                new Relu(),
                new MaxPool2d(2),
                new Flatten(),
                linear1,
                new Relu(),
                linear2
        ));

        try {
            // Conv2d
            double[] floats = WeightLoader.loadWeight("assets/little_ais/data/weight_0");
            List<Tensor> params = conv2d.getParams();
            assert floats.length == params.getFirst().MATRIX.size();
            params.getFirst().MATRIX.fill(floats);
            double[] floats1 = WeightLoader.loadWeight("assets/little_ais/data/weight_1");
            assert floats1.length == params.getLast().MATRIX.size();
            params.getLast().MATRIX.fill(floats1);
            // Linear 1
            params = linear1.getParams();
            double[] floats2 = WeightLoader.loadWeight("assets/little_ais/data/weight_2");
            assert floats2.length == params.getFirst().MATRIX.size();
            params.getFirst().MATRIX.fill(floats2);
            double[] floats3 = WeightLoader.loadWeight("assets/little_ais/data/weight_3");
            assert floats3.length == params.getLast().MATRIX.size();
            params.getLast().MATRIX.fill(floats3);
            // Linear 2
            params = linear2.getParams();
            double[] floats4 = WeightLoader.loadWeight("assets/little_ais/data/weight_4");
            assert floats4.length == params.getFirst().MATRIX.size();
            linear2.getParams().getFirst().MATRIX.fill(floats4);
            double[] floats5 = WeightLoader.loadWeight("assets/little_ais/data/weight_5");
            assert floats5.length == params.getLast().MATRIX.size();
            linear2.getParams().getLast().MATRIX.fill(floats5);
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
