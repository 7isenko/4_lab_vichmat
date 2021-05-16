package io.github._7isenko;

import io.github._7isenko.approximation.ApproximateFunction;
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

    public static void drawPoints(ArrayList<Point> points) {
        ArrayList<ArrayList<Double>> split = excludePoints(points);
        ArrayList<Double> xp = split.get(0);
        ArrayList<Double> yp = split.get(1);

        XYChart chart = new XYChartBuilder().width(600).height(400).title("Your points").xAxisTitle("x").yAxisTitle("y").build();
        chart.addSeries("points", xp, yp).setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        displayChart(chart);
    }

    public static void createFunctionGraphWithPoints(ApproximateFunction chosenApproximateFunction, ArrayList<Point> points, String strFunc) {
        ArrayList<ArrayList<Double>> split = excludePoints(points);
        ArrayList<Double> xp = split.get(0);
        ArrayList<Double> yp = split.get(1);

        XYChart chart = new XYChartBuilder().width(600).height(400).title("Your points").xAxisTitle("x").yAxisTitle("y").build();
        chart.addSeries("points", xp, yp).setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);

        ArrayList<Double> xl = new ArrayList<>();
        double minx = Collections.min(xp) - 0.5;
        double maxx = Collections.max(xp) + 0.5;

        createGraph(chart, "approximated", chosenApproximateFunction, minx, maxx, XYSeries.XYSeriesRenderStyle.Line, Color.BLUE);

        displayChart(chart);
    }

    public static void createIntegralExampleGraph(ApproximateFunction chosenApproximateFunction, String strFunc, double xLeft, double xRight) {
        if (xLeft > xRight) {
            double tmp = xLeft;
            xLeft = xRight;
            xRight = tmp;
        }
        XYChart chart = createFunctionGraphWithPoints(chosenApproximateFunction, strFunc, xLeft - 1, xRight + 1);
        createGraph(chart, "integral", chosenApproximateFunction, xLeft, xRight, XYSeries.XYSeriesRenderStyle.Area, Color.BLUE);
    }

    public static XYChart createFunctionGraphWithPoints(ApproximateFunction approximateFunction, String formula, double leftBorder, double rightBorder) {
        XYChart chart = new XYChartBuilder().width(600).height(400).title(formula).xAxisTitle("x").yAxisTitle("y").build();
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setZoomEnabled(true);
        createGraph(chart, "y(x)", approximateFunction, leftBorder, rightBorder, XYSeries.XYSeriesRenderStyle.Line, Color.GREEN);
        // SwingWrapper swingWrapper = new SwingWrapper<>(chart);
        //  JFrame frame = swingWrapper.displayChart(); // FIXME: моя либа для графиков обожает бросать здесь исключение, причем из другого потока
        JFrame frame = displayChart(chart);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.setAlwaysOnTop(true);
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

    private static JFrame displayChart(XYChart chart) {
        final JFrame frame = new JFrame("Semisenko Max P3232");
        try {
            SwingUtilities.invokeLater(() -> {
                Thread.setDefaultUncaughtExceptionHandler(null);

                JPanel chartPanel = new XChartPanel<>(chart);

                frame.add(chartPanel);
                frame.pack();
                frame.setVisible(true);
            });
        } catch (Exception e) {
            // ignore
        }

        return frame;
    }

    private static ArrayList<ArrayList<Double>> excludePoints(List<Point> points) {
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
