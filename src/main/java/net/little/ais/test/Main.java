package net.little.ais.test;

import net.little.ais.ai.layer.*;
import net.little.ais.ai.loss.CrossEntropy;
import net.little.ais.ai.loss.Loss;
import net.little.ais.ai.loss.MSE;
import net.little.ais.ai.math.BatchMatrix;
import net.little.ais.ai.opt.Optimizer;
import net.little.ais.ai.opt.SGD;
import net.little.ais.ai.tensor.Tensor;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        Layer SEQUENTIAL = new Sequential(List.of(new Linear(2,3), new Tanh(), new Linear(3,2), new Relu(), new Softmax()));

        Tensor inputs = new Tensor(
          new BatchMatrix(4,1,2)
        );
        inputs.MATRIX.fill(
                0,0,
                0,1,
                1,0,
                1,1
        );

        Tensor targets = new Tensor(
                new BatchMatrix(4,1,2)
        );
        targets.MATRIX.fill(
                1,0,
                0,1,
                0,1,
                1,0
        );

        Loss loss = new CrossEntropy();
        Optimizer optimizer = new SGD(SEQUENTIAL.getParams());

        int EPOCHS = 10_000;

        for (int i = 0; i < EPOCHS; i++) {

            Tensor pred = SEQUENTIAL.forward(inputs);
            Tensor l = loss.forward(pred, targets);
            System.out.println(l.MATRIX.sum());
            l.backward();

            optimizer.step(0.1);
            optimizer.zeroGrad();
        }

        System.out.println(SEQUENTIAL.forward(inputs));

    }

}
