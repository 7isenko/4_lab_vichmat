package io.github._7isenko;

import io.github._7isenko.approximation.*;
import io.github._7isenko.matrixsolver.DiagonalDominanceException;

import java.io.File;
import java.io.IOException;
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
        ArrayList<Point> points = InputReader.readPointsFromFile(file);

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
        } catch (DiagonalDominanceException e) {
            System.out.println(e.getMessage());
            System.out.println("Matrix for square approximate function can't be solved");
            return;
        } catch (ArithmeticException e) {
            System.out.println(e.getMessage());
            return;
        }

        double a, b, c;
        a = chosenApproximateFunction.getA();
        b = chosenApproximateFunction.getB();
        c = chosenApproximateFunction.getC();

        System.out.println("Получены коэффициенты:");
        if (c == 0) {
            System.out.printf("a = %f; b = %f\n", a, b);
            System.out.println("Функция имеет вид:");
            System.out.printf(strFunc, a, b);
        } else {
            System.out.printf("a = %f; b = %f; c = %f\n", a, b, c);
            System.out.printf(strFunc, a, b, c);
        }

        GraphBuilder.createFunctionGraphWithPoints(chosenApproximateFunction, points, strFunc);

    }

}
