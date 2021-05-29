package io.github._7isenko;

import io.github._7isenko.approximation.*;
import io.github._7isenko.point.Point;
import org.knowm.xchart.XYChart;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 7isenko
 */
public class Main {

    private static final InputReader inputReader = new InputReader();

    public static void main(String[] args) {
        blockErrorStream();

        List<File> files;
        try {
            files = Files.walk(Paths.get("points"))
                    .filter(f -> f.getFileName().toString().endsWith(".txt"))
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Не могу найти папку points");
            return;
        }

        System.out.println("Файлы в папке points:");
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            System.out.println(i + 1 + " - " + file.getName());
        }

        System.out.println("Выберите номер файла для считывания точек: ");
        int fileNumber = inputReader.readIntFromConsole();
        if (fileNumber > files.size() || fileNumber <= 0) {
            System.out.println("Такого номера нет. Попробуйте еще раз");
            main(args);
            return;
        }


        File file = files.get(fileNumber - 1);
        ArrayList<io.github._7isenko.point.Point> points = InputReader.readPointsFromFile(file);

        GraphBuilder.drawPoints(points);

        System.out.println("Выберите аппроксимирующую функцию");
        System.out.println("1: y = ax + b");
        System.out.println("2: y = ax^2 + bx + c");
        System.out.println("3: y = b * e^(a*x), b > 0");
        System.out.println("4: y = a/x + b");
        int chosenAlgorithm = inputReader.readIntFromConsole();

        if (chosenAlgorithm > 4 || chosenAlgorithm <= 0) {
            System.out.println("Таких я не знаю!");
            return;
        }

        ApproximateFunction chosenApproximateFunction;
        String strFunc;

        switch (chosenAlgorithm) {
            case 1:
                chosenApproximateFunction = new LinearApproximateFunction(points);
                strFunc = "y = %fx + %f";
                break;
            case 2:
                chosenApproximateFunction = new SquareApproximateFunction(points);
                strFunc = "y = %fx^2 + %fx + %f";
                break;
            case 3:
                chosenApproximateFunction = new ExponentialApproximateFunction(points);
                strFunc = "y = %2$f*e^(%1$f*x)";
                break;
            case 4:
                chosenApproximateFunction = new HyperboleApproximateFunction(points);
                strFunc = "y = %f/x + %f";
                break;
            default:
                return;
        }

        try {
            chosenApproximateFunction.calculateCoefficients();
        } catch (ArithmeticException e) {
            System.out.println(e.getMessage());
            return;
        }

        double a_first, b_first, c_first;
        a_first = chosenApproximateFunction.getA();
        b_first = chosenApproximateFunction.getB();
        c_first = chosenApproximateFunction.getC();

        System.out.println("Получены коэффициенты:");
        if (c_first == 0) {
            System.out.printf("a = %f; b = %f\n", a_first, b_first);
            System.out.println("Функция имеет вид:");
            System.out.printf(strFunc + "\n", a_first, b_first);
        } else {
            System.out.printf("a = %f; b = %f; c = %f\n", a_first, b_first, c_first);
            System.out.println("Функция имеет вид:");
            System.out.printf(strFunc + "\n", a_first, b_first, c_first);
        }

        for (Point point : points) {
            System.out.printf("x=%f, y=%f, f=%f, d=%f\n" , point.x, point.y, chosenApproximateFunction.solve(point.x), Math.abs(chosenApproximateFunction.solve(point.x) - point.y));
        }

        XYChart chart = GraphBuilder.createFunctionGraphWithPoints(chosenApproximateFunction, points);

        io.github._7isenko.point.Point remove = points.get(0);
        double inac = Math.abs(remove.y - chosenApproximateFunction.solve(remove.x));
        for (Point point : points) {
            double y = chosenApproximateFunction.solve(point.x);
            double in = Math.abs(y - point.y);
            if (in > inac) {
                inac = in;
                remove = point;
            }
        }

        System.out.println("Хотите ли вы найти точку с наибольшим отклонением?");
        if (inputReader.parseYesOrNo()) {
            System.out.printf("Точка с наибольшим отклонением: x = %f, y = %f\n", remove.x, remove.y);
            System.out.printf("Отклонение от ожидаемого значения равно %f\n", inac);
           GraphBuilder.addRedPoint(remove, chart);

            System.out.println("Удалить её?");
            if (inputReader.parseYesOrNo()) {
                GraphBuilder.removeRedPoint(chart);
                points.remove(remove);
                chosenApproximateFunction.calculateCoefficients();
                GraphBuilder.addFunctionToGraph(chosenApproximateFunction, points, chart);
                System.out.println("Точка удалена");

                double a, b, c;
                a = chosenApproximateFunction.getA();
                b = chosenApproximateFunction.getB();
                c = chosenApproximateFunction.getC();
                System.out.println("Новые значения:");
                if (c == 0) {
                    System.out.printf("a = %f; b = %f\n", a, b);
                    System.out.printf(strFunc + "\n", a, b);

                } else {
                    System.out.printf("a = %f; b = %f; c = %f\n", a, b, c);
                    System.out.printf(strFunc + "\n", a, b, c);
                }
                System.out.println("Прошлые значения:");
                if (c_first == 0) {
                    System.out.printf("a = %f; b = %f\n", a_first, b_first);
                    System.out.printf(strFunc + "\n", a_first, b_first);
                } else {
                    System.out.printf("a = %f; b = %f; c = %f\n", a_first, b_first, c_first);
                    System.out.printf(strFunc + "\n", a_first, b_first, c_first);
                }
            }
        }
    }

    private static void blockErrorStream() {
        try {
            System.setErr(new PrintStream("errors"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
