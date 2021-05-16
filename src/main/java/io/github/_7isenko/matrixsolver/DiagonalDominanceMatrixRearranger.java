package io.github._7isenko.matrixsolver;

import java.util.HashSet;
import java.util.Set;

/**
 * @author 7isenko
 */
public class DiagonalDominanceMatrixRearranger {
    private final double[][] matrix;
    private final double[] bVector;
    private final int size;
    private final Set<Integer> immuneRows;

    private boolean strictColumn;

    public DiagonalDominanceMatrixRearranger(double[][] squareMatrix, double[] bVector) {
        this.matrix = squareMatrix;
        this.bVector = bVector;
        this.size = squareMatrix.length;
        this.immuneRows = new HashSet<>();
    }

    public void rearrange() throws ArithmeticException {
        for (int diagonalIndex = 0; diagonalIndex < size; diagonalIndex++) {
            int dominantElementRowIndex = getDominantElementRowIndex(diagonalIndex, size);
            if (strictColumn) {
                immuneRows.add(diagonalIndex);
            }
            if (matrix[dominantElementRowIndex][diagonalIndex] != matrix[diagonalIndex][diagonalIndex] && isSwapPossible(diagonalIndex, dominantElementRowIndex)) {
                swapRows(diagonalIndex, dominantElementRowIndex);
            } else {
                agileSafeSwap(diagonalIndex, dominantElementRowIndex);
            }
        }
    }

    private void agileSafeSwap(int diagonalIndex, int dominantElementRowIndex) {
        if (matrix[dominantElementRowIndex][diagonalIndex] == matrix[diagonalIndex][diagonalIndex]) {
            return;
        }
        if (!strictColumn) {
            dominantElementRowIndex = getDominantElementRowIndex(diagonalIndex, dominantElementRowIndex);
            if (isSwapPossible(diagonalIndex, dominantElementRowIndex)) {
                swapRows(diagonalIndex, dominantElementRowIndex);
            } else {
                agileSafeSwap(diagonalIndex, dominantElementRowIndex);
            }
        } else {
            throw new DiagonalDominanceException("Diagonal dominance is unavailable");
        }
    }

    private int getDominantElementRowIndex(int columnIndex, int rowBound) {
        int dominantRowIndex = 0;
        strictColumn = true;
        double dominantElement = Math.abs(matrix[dominantRowIndex][columnIndex]);
        for (int rowIndex = 1; rowIndex < rowBound; rowIndex++) {
            double currentElement = Math.abs(matrix[rowIndex][columnIndex]);
            if (currentElement >= dominantElement) {
                if (currentElement == dominantElement) {
                    strictColumn = false;
                }
                if (currentElement > dominantElement) {
                    strictColumn = true;
                }
                dominantRowIndex = rowIndex;
                dominantElement = currentElement;
            }
        }
        return dominantRowIndex;
    }

    private boolean isSwapPossible(int diagonalIndex, int dominantElementRowIndex) {
        if (dominantElementRowIndex < diagonalIndex) {
            return isUpperSafeSwapPossible(diagonalIndex);
        }
        return true;
    }

    /**
     * Метод проверяет ситуацию при нестрогом диагональном преобладании, когда можно попытаться поменять уже отсортированные
     * строки и при этом сохранить диагональное преобладание.
     */
    private boolean isUpperSafeSwapPossible(int diagonalBound) {
        boolean isSafe = true;
        for (int diagonalIndex = 0; diagonalIndex < diagonalBound; diagonalIndex++) {
            if (immuneRows.contains(diagonalIndex)) {
                isSafe = false;
                break;
            }
        }
        return isSafe;
    }

    private void swapRows(int rowIndex1, int rowIndex2) {
        if (rowIndex1 == rowIndex2) return;
        for (int columnIndex = 0; columnIndex < size; columnIndex++) {
            double temp = matrix[rowIndex1][columnIndex];
            matrix[rowIndex1][columnIndex] = matrix[rowIndex2][columnIndex];
            matrix[rowIndex2][columnIndex] = temp;
        }

        double temp = bVector[rowIndex1];
        bVector[rowIndex1] = bVector[rowIndex2];
        bVector[rowIndex2] = temp;
    }
}
