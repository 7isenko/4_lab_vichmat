package io.github._7isenko.approximation;

import io.github._7isenko.Point;

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
        double sxx = sumByFunc(point -> Math.pow(Math.log(point.x), 2));
        double sx = sumByFunc(point -> Math.log(point.x));
        double sy = sumByFunc(point -> Math.log(point.y));
        double sxy = sumByFunc(point -> point.y * Math.log(point.x));

        double d = sxx * size - sx * sx;
        if (d == 0) d += 0.00001;
        double d1 = sxy * size - sx * sy;
        double d2 = sxx * sy - sx * sxy;

        a = d1 / d;
        b = d2 / d;
    }

    @Override
    public double solve(double x) {
        return a * Math.log(x) + b;
    }
}
