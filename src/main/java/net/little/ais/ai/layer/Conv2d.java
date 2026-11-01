package net.little.ais.ai.layer;

import net.little.ais.ai.math.BatchMatrix;
import net.little.ais.ai.math.Matrix;
import net.little.ais.ai.tensor.Tensor;

import java.util.List;

public class Conv2d implements Layer {

    Tensor kernels;
    int f;
    Tensor bias;
    int w, h;
    int wk, hk;
    int wo, ho;
    int channels;
    int stride;

    public Conv2d(int filters, int wk, int hk, int inputR, int inputC, int channels, int stride) {
        this.wk = wk;
        this.hk = hk;
        this.h = inputR;
        this.w = inputC;
        this.channels = channels;
        this.stride = stride;

        this.wo = (this.w-this.wk)/this.stride + 1;
        this.ho = (this.h-this.hk)/this.stride + 1;

        this.kernels = new Tensor(new Matrix(this.wk*this.hk*this.channels, this.f).iterateSelf(_ -> (Math.random()-0.5)*0.1));
        this.f = filters;
        this.bias = new Tensor(new Matrix(1, this.f));
    }

    @Override
    public Tensor forward(Tensor input) {
        Matrix matrix = input.MATRIX;
        BatchMatrix withChannel;
        if (matrix instanceof BatchMatrix bm) {
            withChannel = bm;
        } else {
            withChannel = new BatchMatrix(matrix.r(), matrix.c(), 1);
            withChannel.fill(matrix);
        }

        Matrix reshapedInput = new Matrix(this.wk*this.hk*this.channels, this.wo*this.ho);

        for (int r = 0; r < matrix.r(); r+=this.stride) {
            for (int c = 0; c < matrix.c(); c+=this.stride) {
                for (int i = 0; i < this.hk; i++) {
                    for (int j = 0; j < this.wk; j++) {
                        for (int k = 0; k < this.channels; k++) {
                            int ir = r/this.stride;
                            int ic = c/this.stride;
                            int idx = ir*this.wo+ic;
                            reshapedInput.set(i*j*k, idx, withChannel.get(r+i,c+j, k));
                        }
                    }
                }
            }
        }

        return new Tensor(reshapedInput);
    }

    /*
    C = Channels, F = Filter
    input(W,H,C)
    kernels = kernels(Wk, Hk, C, F) = (Wk*Wh*C, F)
    // outH = (H - Hk) / stride + 1
    // outW = (W - Wk) / stride + 1
    input => (Wk*Hk*C, outW*outH).T => (outW*outH, Wk*Hk*C)
    out = (outW*outH, F)
    */

    @Override
    public List<Tensor> getParams() {
        return List.of();
    }

}
