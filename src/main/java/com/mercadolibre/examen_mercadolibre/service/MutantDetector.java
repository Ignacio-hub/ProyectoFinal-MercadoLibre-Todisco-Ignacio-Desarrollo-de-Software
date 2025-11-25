package com.mercadolibre.examen_mercadolibre.service;

import com.mercadolibre.examen_mercadolibre.exception.InvalidDnaSequenceException;
import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.regex.Pattern;

@Component
public class MutantDetector {

    private static final int SEQUENCE_LENGTH = 4;


    private static final Set<Character> VALID_BASES = Set.of('A', 'T', 'C', 'G');
    private static final Pattern VALID_CHARS_PATTERN = Pattern.compile("^[ATCG]+$");

    public boolean isMutant(String[] dna) {

        if (dna == null || dna.length < SEQUENCE_LENGTH) {
            throw new InvalidDnaSequenceException("La secuencia de ADN no es válida (mínimo NxN donde N>=4).");
        }
        int N = dna.length;

        int mutantSequencesFound = 0;
        char[][] dnaMatrix = new char[N][N];

        for (int i = 0; i < N; i++) {
            if (dna[i] == null || dna[i].length() != N) {
                throw new InvalidDnaSequenceException("La secuencia debe ser una matriz cuadrada (NxN), N>=4.");
            }

            dnaMatrix[i] = dna[i].toCharArray();

            for (char c : dnaMatrix[i]) {
                if (!VALID_BASES.contains(c)) {
                    throw new InvalidDnaSequenceException("La secuencia solo puede contener A, T, C, G.");
                }
            }
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {

                if (j <= N - SEQUENCE_LENGTH) {
                    if (checkHorizontal(dnaMatrix, i, j)) {
                        mutantSequencesFound++;
                        if (mutantSequencesFound > 1) return true;
                    }
                }

                if (i <= N - SEQUENCE_LENGTH) {
                    if (checkVertical(dnaMatrix, i, j)) {
                        mutantSequencesFound++;
                        if (mutantSequencesFound > 1) return true;
                    }
                }

                if (i <= N - SEQUENCE_LENGTH && j <= N - SEQUENCE_LENGTH) {
                    if (checkDiagonalDescending(dnaMatrix, i, j)) {
                        mutantSequencesFound++;
                        if (mutantSequencesFound > 1) return true;
                    }
                }

                if (i >= SEQUENCE_LENGTH - 1 && j <= N - SEQUENCE_LENGTH) {
                    if (checkDiagonalAscending(dnaMatrix, i, j)) {
                        mutantSequencesFound++;
                        if (mutantSequencesFound > 1) return true;
                    }
                }
            }
        }

        return mutantSequencesFound > 1;
    }

    private boolean checkHorizontal(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row][col + 1] == base &&
                matrix[row][col + 2] == base &&
                matrix[row][col + 3] == base;
    }

    private boolean checkVertical(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row + 1][col] == base &&
                matrix[row + 2][col] == base &&
                matrix[row + 3][col] == base;
    }

    private boolean checkDiagonalDescending(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row + 1][col + 1] == base &&
                matrix[row + 2][col + 2] == base &&
                matrix[row + 3][col + 3] == base;
    }

    private boolean checkDiagonalAscending(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];

        return matrix[row - 1][col + 1] == base &&
                matrix[row - 2][col + 2] == base &&
                matrix[row - 3][col + 3] == base;
    }

}