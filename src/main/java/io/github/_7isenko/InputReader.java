package io.github._7isenko;

import io.github._7isenko.point.Point;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

/**
 * @author 7isenko
 */
public class InputReader {

    public static ArrayList<Point> readPointsFromFile(File file) {
        ArrayList<Point> points = new ArrayList<>();
        try {
            BufferedReader inputFile = new BufferedReader(new FileReader(file));
            String line = inputFile.readLine();

            while (line != null) {
                points.add(readPoint(line));
                line = inputFile.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return points;
    }

    public double readDoubleFromConsole() {
        Scanner in = new Scanner(System.in);
        in.useLocale(Locale.US);
        return in.nextDouble();
    }

    public int readIntFromConsole() {
        Scanner in = new Scanner(System.in);
        in.useLocale(Locale.US);
        return in.nextInt();
    }

    public boolean parseYesOrNo() {
        Scanner in = new Scanner(System.in);
        return in.next().startsWith("y");
    }

    private static Point readPoint(String s) {
        String[] split = s.split(" ");
        double x = Double.parseDouble(split[0]);
        double y = Double.parseDouble(split[1]);
        return new Point(x, y);
    }

}
