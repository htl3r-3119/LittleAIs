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
        Conv2d conv2d = new Conv2d(32, 2,2, 28,28,1,1);
        Linear linear = new Linear(5408, 10);

        this.model = new Sequential(List.of(
                conv2d,
                new Relu(),
                new MaxPool2d(2),
                new Flatten(),
                linear
        ));

        try {
            WeightLoader.loadWeight("");
            System.out.println("LOADED!!!!");
        } catch (IOException e) {
            // throw new RuntimeException(e);
        }

    }

    public int predict(Tensor input) {
        Tensor forward = this.model.forward(input);
        Matrix softmax = Functions.softmax(forward.MATRIX);
        return softmax.argMax();
    }

}
