package net.little.ais.ai.model;

import net.little.ais.ai.layer.*;
import net.little.ais.ai.loss.CrossEntropy;
import net.little.ais.ai.loss.Loss;
import net.little.ais.ai.math.Functions;
import net.little.ais.ai.math.Matrix;
import net.little.ais.ai.opt.Optimizer;
import net.little.ais.ai.opt.SGD;
import net.little.ais.ai.tensor.Tensor;

import java.util.List;

public class RedstoneConverterModel {

    Layer model;
    Loss loss;
    Optimizer opt;
    public int EPOCHS;
    public double LR;
    public final DataSet dataSet;

    public RedstoneConverterModel() {
        this.model = new Sequential(List.of(
                new Linear(3,64),
                new Relu(),
                new Linear(64,15)
        ));
        this.loss = new CrossEntropy();
        this.opt = new SGD(this.model.getParams());
        this.EPOCHS = 100;
        this.LR = 0.1;
        this.dataSet = new DataSet();
    }

    public int predict(int a, int b, int c) {
        Matrix i = new Matrix(1,3);
        i.fill(a,b,c);
        return Functions.softmax(this.model.forward(new Tensor(i)).MATRIX).argMax();
    }

    public void train() {
        if (this.dataSet.isEmpty()) return;
        Tensor[] trainingData = this.dataSet.getTrainingData();
        for (int i = 0; i < this.EPOCHS; i++) {
            Tensor logits = this.model.forward(trainingData[0]);
            Tensor loss = this.loss.forward(logits, trainingData[1]);
            loss.backward();

            this.opt.step(this.LR);
            this.opt.zeroGrad();
        }
    }

}
