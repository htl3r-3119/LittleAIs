package net.little.ais.test;

import net.little.ais.ai.layer.*;
import net.little.ais.ai.loss.Loss;
import net.little.ais.ai.loss.MSE;
import net.little.ais.ai.math.Matrix;
import net.little.ais.ai.opt.Optimizer;
import net.little.ais.ai.opt.SGD;
import net.little.ais.ai.tensor.Tensor;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        Layer SEQUENTIAL = new Sequential(List.of(new Linear(2,3), new Tanh(), new Linear(3,1), new Sig()));

        Tensor[] inputs = {
                new Tensor(new Matrix(1,2)),
                new Tensor(new Matrix(1,2)),
                new Tensor(new Matrix(1,2)),
                new Tensor(new Matrix(1,2))
        };
        inputs[0].MATRIX.fill(0,0);
        inputs[1].MATRIX.fill(0,1);
        inputs[2].MATRIX.fill(1,0);
        inputs[3].MATRIX.fill(1,1);

        Tensor[] targets = {
                new Tensor(new Matrix(1,1)),
                new Tensor(new Matrix(1,1)),
                new Tensor(new Matrix(1,1)),
                new Tensor(new Matrix(1,1))
        };
        targets[0].MATRIX.fill(0);
        targets[1].MATRIX.fill(1);
        targets[2].MATRIX.fill(1);
        targets[3].MATRIX.fill(0);

        Loss loss = new MSE();
        Optimizer optimizer = new SGD(SEQUENTIAL.getParams());

        int EPOCHS = 10_000;

        for (int i = 0; i < EPOCHS; i++) {
            int idx = (int)(Math.random()*inputs.length);
            Tensor input = inputs[idx];
            Tensor target = targets[idx];

            Tensor pred = SEQUENTIAL.forward(input);
            Tensor l = loss.forward(pred, target);
            l.backward();

            optimizer.step(0.8);
            optimizer.zeroGrad();
        }

        Arrays.stream(inputs).forEach(tensor -> {
            Tensor forward = SEQUENTIAL.forward(tensor);
            System.out.println(forward);
        });

    }

}
