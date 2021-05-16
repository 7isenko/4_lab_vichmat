package io.github._7isenko;

import io.github._7isenko.approximation.ApproximateFunction;
import io.github._7isenko.point.Point;
import org.knowm.xchart.*;
import org.knowm.xchart.style.markers.SeriesMarkers;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @author 7isenko
 */
public class GraphBuilder {

    public static SwingWrapper<XYChart> currentWrapper;

    public static void drawPoints(ArrayList<io.github._7isenko.point.Point> points) {
        ArrayList<ArrayList<Double>> split = excludePoints(points);
        ArrayList<Double> xp = split.get(0);
        ArrayList<Double> yp = split.get(1);

        XYChart chart = new XYChartBuilder().width(600).height(400).title("Your points").xAxisTitle("x").yAxisTitle("y").build();
        chart.addSeries("points", xp, yp).setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter).setMarkerColor(Color.GREEN);
        currentWrapper = new SwingWrapper<>(chart);
        currentWrapper.displayChart().setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    public static void addRedPoint(io.github._7isenko.point.Point point, XYChart chart) {
        chart.addSeries("bad point", new double[]{point.x}, new double[]{point.y}).setMarkerColor(Color.RED).setMarker(SeriesMarkers.CROSS);
        currentWrapper.repaintChart();
    }

    public static void removeRedPoint(XYChart chart) {
        chart.removeSeries("bad point");
        currentWrapper.repaintChart();
    }

    public static void addFunctionToGraph(ApproximateFunction chosenApproximateFunction, ArrayList<io.github._7isenko.point.Point> points, XYChart chart) {
        ArrayList<ArrayList<Double>> split = excludePoints(points);
        ArrayList<Double> xp = split.get(0);

        double minx = Collections.min(xp) - 0.5;
        double maxx = Collections.max(xp) + 0.5;

        createGraph(chart, "approximated once more", chosenApproximateFunction, minx, maxx, XYSeries.XYSeriesRenderStyle.Line, Color.ORANGE);
        chart.getSeriesMap().get("approximated").setLineColor(Color.GRAY);
        currentWrapper.repaintChart();
    }

    public static XYChart createFunctionGraphWithPoints(ApproximateFunction chosenApproximateFunction, ArrayList<io.github._7isenko.point.Point> points) {
        ArrayList<ArrayList<Double>> split = excludePoints(points);
        ArrayList<Double> xp = split.get(0);
        ArrayList<Double> yp = split.get(1);

        XYChart chart = new XYChartBuilder().width(600).height(400).title("Your points").xAxisTitle("x").yAxisTitle("y").build();

        double minx = Collections.min(xp) - 0.5;
        double maxx = Collections.max(xp) + 0.5;

        createGraph(chart, "approximated", chosenApproximateFunction, minx, maxx, XYSeries.XYSeriesRenderStyle.Line, Color.BLUE);
        chart.addSeries("points", xp, yp).setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter).setMarkerColor(Color.GREEN);

        currentWrapper = new SwingWrapper<>(chart);
        currentWrapper.displayChart();
        return chart;
    }

    private static void createGraph(XYChart chart, String name, ApproximateFunction approximateFunction, double leftBorder, double rightBorder, XYSeries.XYSeriesRenderStyle renderStyle, Color lineColor) {
        if (leftBorder > rightBorder) {
            double tmp = leftBorder;
            leftBorder = rightBorder;
            rightBorder = tmp;
        }
        ArrayList<Double> xGraph = new ArrayList<>();
        ArrayList<Double> yGraph = new ArrayList<>();
        double xVal = leftBorder;
        while (xVal <= rightBorder) {
            double yVal = approximateFunction.solve(xVal);
            if (Double.isFinite(yVal) && yVal < 100000D) {
                double last = yGraph.size() > 0 ? yGraph.get(yGraph.size() - 1) : yVal;
                if (Math.min(last, yVal) - Math.max(yVal, last) >= -195) {
                    xGraph.add(xVal);
                    yGraph.add(yVal);
                } else {
                    createGraph(chart, name + "2", approximateFunction, xVal, rightBorder, renderStyle, lineColor);
                    break;
                }
            } else {
                if (Double.isNaN(yVal)) {
                    xGraph.add(xVal);
                    yGraph.add(approximateFunction.solve(xVal + 0.0001));
                } else {
                    if (Double.isInfinite(yVal)) {
                        createGraph(chart, name + "2", approximateFunction, xVal + 0.0001, rightBorder, renderStyle, lineColor);
                        break;
                    }
                }
            }
            xVal += 0.005;
        }
        try {
            if (Collections.max(yGraph) > 200D) {
                chart.getStyler().setYAxisMax(8D);
            }
        } catch (NoSuchElementException e) {
            // ignore, it is ok
        }
        try {
            if (Collections.min(yGraph) < -200D) {
                chart.getStyler().setYAxisMin(-8D);
            }
        } catch (NoSuchElementException e) {
            // ignore
        }

        if (!xGraph.isEmpty()) {
            chart.addSeries(name, xGraph, yGraph).setXYSeriesRenderStyle(renderStyle).setMarker(SeriesMarkers.NONE).setLineColor(lineColor);
        }
    }

    private static ArrayList<ArrayList<Double>> excludePoints(List<io.github._7isenko.point.Point> points) {
        ArrayList<ArrayList<Double>> rez = new ArrayList<>();
        rez.add(new ArrayList<>());
        rez.add(new ArrayList<>());

        for (Point point : points) {
            rez.get(0).add(point.x);
            rez.get(1).add(point.y);
        }
        return rez;
    }
}
