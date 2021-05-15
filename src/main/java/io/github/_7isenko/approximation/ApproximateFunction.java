package io.github._7isenko.approximation;

import io.github._7isenko.Point;
import io.github._7isenko.PointFunction;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

/**
 * @author 7isenko
 */
@RequiredArgsConstructor
public abstract class ApproximateFunction {
    @Getter
    protected double a, b, c;

    private final ArrayList<Point> points;
    protected final int size = points.size();

    public abstract void calculateCoefficients();

    public abstract double solve(double x);

    protected double sumByFunc(PointFunction function) {
        double sum = 0;
        for (Point point : points) {
            sum += function.solve(point);
        }
        return sum;
    }
}
