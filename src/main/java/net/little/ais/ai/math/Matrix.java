package net.little.ais.ai.math;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Matrix {

    int r, c;
    double[] vals;

    public Matrix(int r, int c) {
        this.r = r;
        this.c = c;
        this.vals = new double[r*c];
    }

    public double get(int r, int c) {
        return this.vals[this.c*r+c];
    }

    public void set(int r, int c, double d) {
        this.vals[r*this.c+c] = d;
    }

    public boolean fitForSimpleOp(Matrix other) {
        return this.r == other.r && this.c == other.c;
    }

    public boolean fitForMatMul(Matrix other) {
        return this.c == other.r;
    }

    public Matrix iterateSelf(Function<Double, Double> func) {
        for (int idx = 0; idx < this.vals.length; idx++) {
            this.vals[idx] = func.apply(this.vals[idx]);
        }
        return this;
    }

    public Matrix iterateSelf(BiFunction<Double, Double, Double> func, Matrix other) {
        if (!this.fitForSimpleOp(other)) {
            throw new RuntimeException("The dimensions don't fit");
        }
        for (int idx = 0; idx < this.vals.length; idx++) {
            this.vals[idx] = func.apply(this.vals[idx], other.vals[idx]);
        }
        return this;
    }

    public Matrix iterateTo(Function<Double, Double> func, Matrix to) {
        if (!this.fitForSimpleOp(to)) {
            throw new RuntimeException("The dimensions don't fit");
        }
        for (int idx = 0; idx < this.vals.length; idx++) {
            to.vals[idx] = func.apply(this.vals[idx]);
        }
        return to;
    }

    public Matrix iterateTo(BiFunction<Double, Double, Double> func, Matrix other, Matrix to) {
        if (!(this.fitForSimpleOp(other) || this.fitForSimpleOp(to))) {
            throw new RuntimeException("The dimensions don't fit");
        }
        for (int idx = 0; idx < this.vals.length; idx++) {
            to.vals[idx] = func.apply(this.vals[idx], other.vals[idx]);
        }
        return to;
    }

    public Matrix matmul(Matrix other, Matrix out) {
        if (!this.fitForMatMul(other) || this.r != out.r || other.c != out.c) {
            throw new RuntimeException("Dimensions don't fit matrix multiplication");
        }
        for (int i = 0; i < this.r; i++) {
            for (int j = 0; j < other.c; j++) {
                double sum = 0;
                for (int k = 0; k < this.c; k++) {
                    sum += this.get(i,k) * other.get(k,j);
                }
                out.set(i,j,sum);
            }
        }
        return out;
    }

    public Matrix transposeSelf() {
        double[] arr = new double[this.vals.length];
        for (int r = 0; r < this.r; r++) {
            for (int c = 0; c < this.c; c++) {
                int idxA = r*this.c+c;
                int idxB = c*this.r+r;
                arr[idxB] = this.vals[idxA];
            }
        }
        this.c ^= this.r;
        this.r ^= this.c;
        this.c ^= this.r;
        System.arraycopy(arr, 0, this.vals, 0, this.vals.length);
        return this;
    }

    public Matrix transposeTo(Matrix dest) {
        if (this.vals.length != dest.vals.length) {
            throw new RuntimeException("Dimensions don't fit");
        }
        for (int r = 0; r < this.r; r++) {
            for (int c = 0; c < this.c; c++) {
                dest.set(c,r, this.get(r,c));
            }
        }
        return dest;
    }

    public void fill(double... vals) {
        System.arraycopy(vals, 0, this.vals, 0, vals.length);
    }

    @Override
    public String toString() {
        StringBuilder strB = new StringBuilder();
        strB.append("{\n");
        for (int r = 0; r < this.r; r++) {
            strB.append('[');
            for (int c = 0; c < this.c-1; c++) {
                strB.append(this.get(r,c)).append(", ");
            }
            strB.append(this.get(r,this.c-1)).append("]\n");
        }
        strB.append("}");
        return strB.toString();
    }
}
