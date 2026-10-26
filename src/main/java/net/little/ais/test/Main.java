package net.little.ais.test;

import net.little.ais.ai.layer.*;
import net.little.ais.ai.loss.CrossEntropy;
import net.little.ais.ai.loss.Loss;
import net.little.ais.ai.loss.MSE;
import net.little.ais.ai.math.BatchMatrix;
import net.little.ais.ai.math.Functions;
import net.little.ais.ai.opt.Optimizer;
import net.little.ais.ai.opt.SGD;
import net.little.ais.ai.tensor.CrossEntropyTensor;
import net.little.ais.ai.tensor.Tensor;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        Layer SEQUENTIAL = new Sequential(List.of(new Linear(2,3), new Tanh(), new Linear(3,2)));

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

        int EPOCHS = 100_000;

        boolean cond = false;

        while (!cond) {

            Tensor pred = SEQUENTIAL.forward(inputs);
            CrossEntropyTensor l = (CrossEntropyTensor) loss.forward(pred, targets);
            double temp = l.MATRIX.sum();
            cond = temp <= 0.003;
            System.out.println(temp);
            l.backward();
            /*System.out.println("##### PARAMS #####");
            System.out.println(SEQUENTIAL.getParams().stream().map(tensor -> tensor.GRAD).toList());
            System.out.println("##################");
            System.exit(0);*/

            optimizer.step(0.1);
            optimizer.zeroGrad();
        }

        System.out.println(Functions.softmax(SEQUENTIAL.forward(inputs).MATRIX));

    }

}
