package net.little.ais.ai.math;

public final class Functions {

    public static double ADD(double a , double b) { return a+b; }
    public static double SUB(double a , double b) { return a-b; }
    public static double MUL(double a , double b) { return a*b; }
    public static double DIV(double a , double b) { return a/b; }

    public static double SIG(double x) { return 1.0/(1.0 + Math.exp(-x)); }
    public static double RELU(double x) { return x > 0 ? x : 0; }
    public static double TANH(double x) { return (Math.exp(2.*x)-1.)    / (Math.exp(2.*x)+1.); }

    public static double SIGD(double y) { return 1. - y; }
    public static double RELUD(double x) { return x > 0 ? 1. : 0.; }
    public static double TANHD(double y) { return 1. - y*y; }

}
