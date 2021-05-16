package io.github._7isenko.approximation;

import io.github._7isenko.point.Point;

import java.util.ArrayList;

/**
 * @author 7isenko
 */
public class SquareApproximateFunction extends ApproximateFunction {

    public SquareApproximateFunction(ArrayList<Point> points) {
        super(points);
    }

    @Override
    public void calculateCoefficients() throws ArithmeticException {
        double x = sumByFunc(p -> p.x);
        double x2 = sumByFunc(p -> p.x * p.x);
        double x3 = sumByFunc(p -> p.x * p.x * p.x);
        double x4 = sumByFunc(p -> p.x * p.x * p.x * p.x);
        double y = sumByFunc(p -> p.y);
        double xy = sumByFunc(p -> p.x * p.y);
        double x2y = sumByFunc(p -> p.x * p.x * p.y);

        double[][] matrix = new double[][]{{(double) size, x, x2}, {x, x2, x3}, {x2, x3, x4}};
        double[] bVector = new double[]{y, xy, x2y};

        double[] rez = lsolve(matrix, bVector);
        c = rez[0];
        b = rez[1];
        a = rez[2];
    }

    @Override
    public double solve(double x) {
        return a * x * x + b * x + c;
    }

    // Gaussian elimination with partial pivoting
    public static double[] lsolve(double[][] A, double[] b) {
        int n = b.length;

        for (int p = 0; p < n; p++) {

            // find pivot row and swap
            int max = p;
            for (int i = p + 1; i < n; i++) {
                if (Math.abs(A[i][p]) > Math.abs(A[max][p])) {
                    max = i;
                }
            }
            double[] temp = A[p];
            A[p] = A[max];
            A[max] = temp;
            double t = b[p];
            b[p] = b[max];
            b[max] = t;

            // singular or nearly singular
            if (Math.abs(A[p][p]) <= 0.00001) {
                throw new ArithmeticException("Matrix is singular or nearly singular");
            }

            // pivot within A and b
            for (int i = p + 1; i < n; i++) {
                double alpha = A[i][p] / A[p][p];
                b[i] -= alpha * b[p];
                for (int j = p; j < n; j++) {
                    A[i][j] -= alpha * A[p][j];
                }
            }
        }

        // back substitution
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < n; j++) {
                sum += A[i][j] * x[j];
            }
            x[i] = (b[i] - sum) / A[i][i];
        }
        return x;
    }
}
