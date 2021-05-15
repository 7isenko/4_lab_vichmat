package io.github._7isenko;

import io.github._7isenko.approximation.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.*;

/**
 * @author 7isenko
 */
public class Main {

    private static final InputReader inputReader = new InputReader();

    public static void main(String[] args) {

        List<File> files;
        try {
            files = Files.walk(Paths.get("/points"))
                    .filter(f -> f.endsWith(".txt"))
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

        GraphBuilder.drawPoints(points); // TODO: implement

        System.out.println("Выберите аппроксимирующую функцию");
        System.out.println("1: y = ax + b");
        System.out.println("2: y = ax^2 + bx + c");
        System.out.println("3: y = a^x + b");
        System.out.println("4: y = a*lnx + b");
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
                strFunc = "y = %fx + %fb";
                break;
            case 2:
                chosenApproximateFunction = new SquareApproximateFunction(points);
                strFunc = "y = %fx^2 + %fx + %f";
                break;
            case 3:
                chosenApproximateFunction = new ExponentialApproximateFunction(points);
                strFunc = "y = %f^x + %f";
                break;
            case 4:
                chosenApproximateFunction = new LogApproximateFunction(points);
                strFunc = "y = %f*ln(x) + %f";
                break;
            default:
                return;
        }

        double xLeft = -8, xRight = 8;

        GraphBuilder.createExampleGraph(chosenApproximateFunction, strFunc, xLeft, xRight);
        System.out.println("Посмотрите на выбранный график и выберите границы интегрирования");

        do {
            System.out.println("Левая граница: ");
            xLeft = inputReader.readDoubleFromConsole();
            System.out.println("Правая граница: ");
            xRight = inputReader.readDoubleFromConsole();

            GraphBuilder.createIntegralExampleGraph(chosenApproximateFunction, strFunc, xLeft, xRight);

            System.out.println("Вы хотите изменить выбранные границы? y/n");
        } while (inputReader.parseYesOrNo());

        double accuracy;
        System.out.println("Введите точность: ");

    }

}
