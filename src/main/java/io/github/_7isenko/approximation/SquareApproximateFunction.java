package io.github._7isenko.approximation;

import io.github._7isenko.Point;
import io.github._7isenko.matrixsolver.DiagonalDominanceMatrixRearranger;
import io.github._7isenko.matrixsolver.SimpleIterationsCalculator;

import java.util.ArrayList;
import java.util.Arrays;

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

        System.out.println(Arrays.deepToString(matrix));

        new DiagonalDominanceMatrixRearranger(matrix, bVector).rearrange();

        double accuracy = 0.001;
        SimpleIterationsCalculator calculator = new SimpleIterationsCalculator(matrix, bVector, accuracy);
        calculator.calculate();
        double[] rez = calculator.getAnswer();
        c = rez[0];
        b = rez[1];
        a = rez[2];
    }

    @Override
    public double solve(double x) {
        return a * x * x + b * x + c;
    }
}
