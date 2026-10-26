package net.little.ais.ai.math;

import java.util.function.BiFunction;
import java.util.function.Function;

public class BatchMatrix extends Matrix {

    int b;

    public BatchMatrix(int b, int r, int c) {
        super(r*b, c);
        this.b = b;
        this.r /= b;
    }

    public double get(int b, int r, int c) {
        return this.vals[b*this.r*this.c+r*this.c+c];
    }

    public void set(int b, int r, int c, double d) {
        this.vals[b*this.r*this.c+r*this.c+c] = d;
    }

    public int b() {
        return this.b;
    }

    public static BatchMatrix onesLike(int b, int r, int c) {
        BatchMatrix m = new BatchMatrix(b,r,c);
        m.iterateSelf(_ -> 1.);
        return m;
    }

    @Override
    public boolean fitForSimpleOp(Matrix other) {
        return super.fitForSimpleOp(other) && !(other instanceof BatchMatrix batch && batch.b != this.b);
    }

    @Override
    public boolean fitForMatMul(Matrix other) {
        return super.fitForMatMul(other) && !(other instanceof BatchMatrix bm && (bm.b != 1 && bm.b != this.b));
    }

    @Override
    public Matrix matmul(Matrix other) {
        if (!this.fitForMatMul(other)) {
            throw new RuntimeException("Dimensions don't fit matrix multiplication");
        }
        if (other instanceof BatchMatrix bm) {
            return matmul(this, bm);
        }
        BatchMatrix m = new BatchMatrix(this.b, this.r, other.c);
        for (int b = 0; b < this.b; b++) {
            for (int i = 0; i < this.r; i++) {
                for (int j = 0; j < other.c; j++) {
                    double sum = 0;
                    for (int k = 0; k < this.c; k++) {
                        sum += this.get(b,i,k) * other.get(k,j);
                    }
                    m.set(b, i, j, sum);
                }
            }
        }
        return m;
    }

    public static BatchMatrix matmul(BatchMatrix x, BatchMatrix y) {
        BatchMatrix m = new BatchMatrix(x.b, x.r, y.c);
        for (int b = 0; b < m.b; b++) {
            for (int i = 0; i < m.r; i++) {
                for (int j = 0; j < m.c; j++) {
                    double sum = 0;
                    for (int k = 0; k < x.c; k++) {
                        sum += x.get(b, i, k) * y.get(b, k, j);
                    }
                    m.set(b, i, j, sum);
                }
            }
        }
        return m;
    }

    @Override
    public Matrix transpose() {
        BatchMatrix m = new BatchMatrix(this.b, this.c, this.r);
        for (int i = 0; i < this.b; i++) {
            for (int j = 0; j < this.r; j++) {
                for (int k = 0; k < this.c; k++) {
                    m.set(i,k,j,this.get(i,j,k));
                }
            }
        }
        return m;
    }

    @Override
    public Matrix clone() {
        BatchMatrix copy = new BatchMatrix(this.b,this.r,this.c);
        copy.fill(this.vals);
        return copy;
    }

    @Override
    public BatchMatrix iterateSelf(BiFunction<Double, Double, Double> func, Matrix other) {
        if (!this.fitForSimpleOp(other)) {
            throw new RuntimeException(String.format("The dimensions don't fit (%d, %d) | (%d, %d)", this.r, this.c, other.r, other.c));
        }
        if (other instanceof BatchMatrix) {
            for (int idx = 0; idx < this.vals.length; idx++) {
                this.vals[idx] = func.apply(this.vals[idx], other.vals[idx]);
            }
            return this;
        }
        for (int b = 0; b < this.b; b++) {
            int off = b*this.r*this.c;
            for (int idx = 0; idx < other.vals.length; idx++) {
                this.vals[off+idx] = func.apply(this.vals[off+idx], other.vals[idx]);
            }
        }
        return this;
    }

    @Override
    public String toString() {
        StringBuilder strB = new StringBuilder();
        strB.append("{\n");
        for (int b = 0; b < this.b; b++) {
            for (int r = 0; r < this.r; r++) {
                strB.append('[');
                for (int c = 0; c < this.c - 1; c++) {
                    strB.append(this.get(b, r, c)).append(", ");
                }
                strB.append(this.get(b, r, this.c - 1)).append("]\n");
            }
        }
        strB.append("}");
        return strB.toString();
    }

    public BatchMatrix iterMatrixSelf(Function<Matrix, Matrix> iter) {
        Matrix m = new Matrix(this.r, this.c);
        for (int i = 0; i < this.b; i++) {
            int idx = i * this.r * this.c;
            System.arraycopy(this.vals, idx, m.vals, 0, m.vals.length);
            Matrix out = iter.apply(m);
            System.arraycopy(out.vals, 0, this.vals, idx, out.vals.length);
        }
        return this;
    }

    public Matrix sumPerBatch() {
        BatchMatrix batchMatrix = new BatchMatrix(this.b, 1,1);
        for (int i = 0; i < this.b; i++) {
            int off = i*this.r*this.c;
            double sum = 0;
            for (int j = 0; j < this.r*this.c; j++) {
                sum+=this.vals[off+j];
            }
            batchMatrix.set(i,0,0,sum);
        }
        return batchMatrix;
    }
}
