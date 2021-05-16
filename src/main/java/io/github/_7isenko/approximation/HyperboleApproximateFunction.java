package io.github._7isenko.approximation;

import io.github._7isenko.point.Point;

import java.util.ArrayList;

/**
 * @author 7isenko
 */
public class HyperboleApproximateFunction extends ApproximateFunction {

    public HyperboleApproximateFunction(ArrayList<Point> points) {
        super(points);
    }

    @Override
    public void calculateCoefficients() {
        double sxx = sumByFunc(point -> 1 / (point.x * point.x));
        double sx = sumByFunc(point -> 1 / point.x);
        double sy = sumByFunc(point -> point.y);
        double sxy = sumByFunc(point -> (1 / point.x) * point.y);

        double d = sxx * size - sx * sx;
        if (d == 0) d += 0.00001;
        double d1 = sxy * size - sx * sy;
        double d2 = sxx * sy - sx * sxy;

        a = d1 / d;
        b = d2 / d;
    }

    @Override
    public double solve(double x) {
        return a/x + b;
    }
}
