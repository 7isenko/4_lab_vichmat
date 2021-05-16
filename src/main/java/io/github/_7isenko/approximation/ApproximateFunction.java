package io.github._7isenko.approximation;

import io.github._7isenko.point.Point;
import io.github._7isenko.point.PointFunction;
import io.github._7isenko.point.PointLogicalFunction;
import lombok.Getter;

import java.util.ArrayList;

/**
 * @author 7isenko
 */
public abstract class ApproximateFunction {
    @Getter
    protected double a, b, c;

    private final ArrayList<Point> points;
    protected final int size;

    protected ApproximateFunction(ArrayList<Point> points) {
        this.points = points;
        this.size = points.size();
    }


    public abstract void calculateCoefficients();

    public abstract double solve(double x);

    protected double sumByFunc(PointFunction function) {
        double sum = 0;
        for (Point point : points) {
            sum += function.solve(point);
        }
        return sum;
    }

    protected boolean checkByFunc(PointLogicalFunction function) {
        for (Point point : points) {
            if (!function.solve(point)) {
                return false;
            }
        }
        return true;
    }
}
