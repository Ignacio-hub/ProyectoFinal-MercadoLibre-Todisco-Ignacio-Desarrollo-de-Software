package com.mercadolibre.examen_mercadolibre.detector;

import com.mercadolibre.examen_mercadolibre.exception.InvalidDnaSequenceException;
import org.springframework.stereotype.Component;

@Component
public class MutantDetector {

    private static final int SEQUENCE_LENGTH = 4;

    public boolean isMutant(String[] dna) {

        if (dna == null || dna.length < SEQUENCE_LENGTH) {

            throw new InvalidDnaSequenceException("La secuencia de ADN no es válida (mínimo NxN donde N>=4).");
        }
        int N = dna.length;

        for (String row : dna) {
            if (row.length() != N || row.length() < SEQUENCE_LENGTH) {

                throw new InvalidDnaSequenceException("La secuencia debe ser una matriz cuadrada (NxN), N>=4.");
            }
            if (!row.matches("[ATCG]+")) {

                throw new InvalidDnaSequenceException("La secuencia solo puede contener A, T, C, G.");
            }
        }

        int mutantSequencesFound = 0;

        char[][] dnaMatrix = new char[N][N];
        for (int i = 0; i < N; i++) {
            dnaMatrix[i] = dna[i].toCharArray();
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j <= N - SEQUENCE_LENGTH; j++) {

                if (isSequenceAt(dnaMatrix, i, j, 0, 1, N)) {
                    mutantSequencesFound++;
                    if (mutantSequencesFound > 1) return true;
                }

                if (isSequenceAt(dnaMatrix, j, i, 1, 0, N)) {
                    mutantSequencesFound++;
                    if (mutantSequencesFound > 1) return true;
                }
            }
        }

        for (int i = 0; i <= N - SEQUENCE_LENGTH; i++) {
            for (int j = 0; j <= N - SEQUENCE_LENGTH; j++) {

                if (isSequenceAt(dnaMatrix, i, j, 1, 1, N)) {
                    mutantSequencesFound++;
                    if (mutantSequencesFound > 1) return true;
                }

                if (isSequenceAt(dnaMatrix, i, N - 1 - j, 1, -1, N)) {
                    mutantSequencesFound++;
                    if (mutantSequencesFound > 1) return true;
                }
            }
        }

        return mutantSequencesFound > 1;
    }


    private boolean isSequenceAt(char[][] matrix, int row, int col, int dR, int dC, int N) {

        if (row < 0 || row >= N || col < 0 || col >= N) {
            return false;
        }

        char firstChar = matrix[row][col];
        int count = 1;

        for (int k = 1; k < SEQUENCE_LENGTH; k++) {
            int nextRow = row + dR * k;
            int nextCol = col + dC * k;

            if (nextRow < 0 || nextRow >= N || nextCol < 0 || nextCol >= N) {
                return false;
            }

            if (matrix[nextRow][nextCol] == firstChar) {
                count++;
            } else {
                return false;
            }
        }
        return count == SEQUENCE_LENGTH;
    }
}