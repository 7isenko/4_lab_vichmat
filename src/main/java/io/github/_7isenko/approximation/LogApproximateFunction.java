package io.github._7isenko.approximation;

import io.github._7isenko.Point;

import java.util.ArrayList;

/**
 * @author 7isenko
 */
public class LogApproximateFunction extends ApproximateFunction {

    public LogApproximateFunction(ArrayList<Point> points) {
        super(points);
    }

    @Override
    public void calculateCoefficients() {
        double sxx = sumByFunc(point -> point.x * point.x);
        double sx = sumByFunc(point -> point.x);
        double sy = sumByFunc(point -> Math.log(point.y));
        double sxy = sumByFunc(point -> point.x * Math.log(point.y));

        double d = sxx * size - sx * sx;
        double d1 = sxy * size - sx * sy;
        double d2 = sxx * sy - sx * sxy;

        a = d1 / d;
        b = Math.pow(Math.E, d2 / d);
    }

    @Override
    public double solve(double x) {
        return a * Math.log(x) + b;
    }
}
