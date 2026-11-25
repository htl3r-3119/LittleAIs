package net.little.ais.ai.math;

public class StretchedMatrix extends Matrix {

    int stretch;

    public StretchedMatrix(int r, Matrix m) {
        super(m.r, m.c);
        fill(m);
        this.stretch = r;
    }

    @Override
    public int r() {
        return this.r*stretch;
    }

    @Override
    public double get(int r, int c) {
        return super.get(r/this.stretch, c);
    }

    @Override
    public void set(int r, int c, double d) {
        super.set(r/this.stretch, c, d);
    }

    @Override
    public Matrix clone() {
        return new StretchedMatrix(this.stretch, this);
    }

}
