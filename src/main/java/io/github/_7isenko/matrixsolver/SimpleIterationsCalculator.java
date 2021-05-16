package io.github._7isenko.matrixsolver;

/**
 * @author 7isenko
 */
public class SimpleIterationsCalculator {

    private final double[][] matrix;
    private final double[] bVector;
    private final int size;
    private final double accuracy;

    private final double[] answer;
    private final double[] inaccuracy; // Epsilon vector
    private int iterationsCount = 0;
    private double[] tmpX;

    public SimpleIterationsCalculator(double[][] squareMatrix, double[] bVector, double accuracy) {
        this.matrix = squareMatrix;
        this.bVector = bVector;
        this.accuracy = accuracy;
        this.size = squareMatrix.length;

        answer = new double[size];
        inaccuracy = new double[size];
        tmpX = new double[size];
    }


    public void calculate() {
        calculateZeroVector();
        do {
            for (int i = 0; i < size; i++) {
                answer[i] = bVector[i] / matrix[i][i];
                for (int j = 0; j < size; j++) {
                    if (i != j) {
                        answer[i] -= matrix[i][j] / matrix[i][i] * tmpX[j];
                    }
                }
                inaccuracy[i] = Math.abs(tmpX[i] - answer[i]);
            }

            tmpX = answer.clone();
            iterationsCount++;

        } while (couldIterate());
    }

    private boolean couldIterate() {
        return getMaxInaccuracy() >= accuracy;
    }

    private void calculateZeroVector() {
        for (int i = 0; i < size; i++) {
            tmpX[i] = bVector[i] / matrix[i][i];
        }
    }

    private double getMaxInaccuracy() {
        double maxInaccuracy = Math.abs(inaccuracy[0]);
        for (int i = 1; i < size; i++) {
            double currentInaccuracy = Math.abs(inaccuracy[i]);
            if (currentInaccuracy > maxInaccuracy) {
                maxInaccuracy = currentInaccuracy;
            }
        }
        return maxInaccuracy;
    }

    public double[] getAnswer() {
        return answer;
    }

    public double[] getInaccuracy() {
        return inaccuracy;
    }

    public int getIterationsCount() {
        return iterationsCount;
    }
}
