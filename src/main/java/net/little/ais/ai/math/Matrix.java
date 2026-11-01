package net.little.ais.ai.math;

import java.util.Arrays;
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
        return this.vals[this.c()*r+c];
    }

    public void set(int r, int c, double d) {
        this.vals[r*this.c()+c] = d;
    }

    public boolean fitForSimpleOp(Matrix other) {
        return this.r() == other.r() && this.c() == other.c();
    }

    public boolean fitForMatMul(Matrix other) {
        return this.c() == other.r();
    }

    public Matrix iterateSelf(Function<Double, Double> func) {
        for (int idx = 0; idx < this.vals.length; idx++) {
            this.vals[idx] = func.apply(this.vals[idx]);
        }
        return this;
    }

    @Override
    public Matrix clone() {
        Matrix m = new Matrix(this.r(), this.c());
        m.fill(this.vals);
        return m;
    }

    public Matrix add(Matrix other) {
        return this.clone().iterateSelf(Functions::ADD, other);
    }

    public Matrix sub(Matrix other) {
        return this.clone().iterateSelf(Functions::SUB, other);
    }

    public Matrix mul(Matrix other) {
        return this.clone().iterateSelf(Functions::MUL, other);
    }

    public Matrix div(Matrix other) {
        return this.clone().iterateSelf(Functions::DIV, other);
    }

    public Matrix iterateSelf(BiFunction<Double, Double, Double> func, Matrix other) {
        if (!this.fitForSimpleOp(other)) {
            throw new RuntimeException(String.format("The dimensions don't fit (%d, %d) | (%d, %d)", this.r, this.c, other.r, other.c));
        }
        if (other instanceof BatchMatrix bm) {
            for (int b = 0; b < bm.b(); b++) {
                for (int r = 0; r < this.r(); r++) {
                    for (int c = 0; c < this.c(); c++) {
                        this.set(r,c,func.apply(this.get(r,c), bm.get(b,r,c)));
                    }
                }
            }
            return this;
        }
        for (int r = 0; r < this.r(); r++) {
            for (int c = 0; c < this.c(); c++) {
                this.set(r,c,func.apply(this.get(r,c), other.get(r,c)));
            }
        }
        return this;
    }

    public Matrix matmul(Matrix other) {
        if (!this.fitForMatMul(other)) {
            throw new RuntimeException("Dimensions don't fit matrix multiplication");
        }
        if (other instanceof BatchMatrix bm) {
            BatchMatrix m = new BatchMatrix(bm.b(), this.r(), other.c());
            for (int b = 0; b < m.b(); b++) {
                for (int i = 0; i < this.r(); i++) {
                    for (int j = 0; j < other.c(); j++) {
                        double sum = 0;
                        for (int k = 0; k < this.c(); k++) {
                            sum += this.get(i,k) * bm.get(b,k,j);
                        }
                        m.set(b, i, j, sum);
                    }
                }
            }
            return m;
        }
        Matrix m = new Matrix(this.r(), other.c());
        for (int i = 0; i < this.r(); i++) {
            for (int j = 0; j < other.c(); j++) {
                double sum = 0;
                for (int k = 0; k < this.c(); k++) {
                    sum += this.get(i,k) * other.get(k,j);
                }
                m.set(i,j,sum);
            }
        }
        return m;
    }

    public Matrix transpose() {
        Matrix m = new Matrix(this.c(), this.r());
        for (int r = 0; r < this.r(); r++) {
            for (int c = 0; c < this.c(); c++) {
                m.set(c,r,this.get(r,c));
            }
        }
        return m;
    }

    public void fill(double... vals) {
        System.arraycopy(vals, 0, this.vals, 0, vals.length);
    }

    public void fill(Matrix m) {
        System.arraycopy(m.vals, 0, this.vals, 0, m.vals.length);
    }

    @Override
    public String toString() {
        StringBuilder strB = new StringBuilder(String.format("(%dx%d)", this.r(), this.c()));
        strB.append("{\n");
        for (int r = 0; r < this.r(); r++) {
            strB.append('[');
            for (int c = 0; c < this.c()-1; c++) {
                strB.append(this.get(r,c)).append(", ");
            }
            strB.append(this.get(r,this.c()-1)).append("]\n");
        }
        strB.append("}");
        return strB.toString();
    }

    public static Matrix onesLike(int r, int c) {
        Matrix m = new Matrix(r,c);
        m.iterateSelf(_ -> 1.);
        return m;
    }

    public int r() {
        return this.r;
    }

    public int c() {
        return this.c;
    }

    public double sum() {
        return Arrays.stream(this.vals).sum();
    }

    public double max() {
        return Arrays.stream(this.vals).max().orElse(0.);
    }

    public int argMax() {
        int idx = 0;
        double max = this.vals[0];
        for (int i = 1; i < this.vals.length; i++) {
            double val = this.vals[i];
            if (val > max) {
                idx = i;
                max = val;
            }
        }
        return idx;
    }
}
