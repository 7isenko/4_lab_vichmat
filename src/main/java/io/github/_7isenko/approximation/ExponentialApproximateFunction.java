package io.github._7isenko.approximation;

import io.github._7isenko.point.Point;

import java.util.ArrayList;

/**
 * @author 7isenko
 */
public class ExponentialApproximateFunction extends ApproximateFunction {
    public ExponentialApproximateFunction(ArrayList<Point> points) {
        super(points);
    }

    @Override
    public void calculateCoefficients() {

        if (!checkByFunc(point -> point.y > 0)) {
            throw new ArithmeticException("y can't be equal or below zero");
        }

        double sxx = sumByFunc(point -> point.x * point.x);
        double sx = sumByFunc(point -> point.x);
        double sy = sumByFunc(point -> Math.log(point.y));
        double sxy = sumByFunc(point -> point.x * Math.log(point.y));

        double d = sxx * size - sx * sx;
        if (d == 0) d += 0.00001;
        double d1 = sxy * size - sx * sy;
        double d2 = sxx * sy - sx * sxy;

        a = d1 / d;
        b = Math.pow(Math.E, d2 / d);
    }

    @Override
    public double solve(double x) {
        return b * Math.pow(Math.E, a*x);
    }

}
