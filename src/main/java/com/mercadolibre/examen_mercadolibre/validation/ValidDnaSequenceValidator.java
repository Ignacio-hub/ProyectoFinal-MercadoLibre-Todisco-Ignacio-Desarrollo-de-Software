package com.mercadolibre.examen_mercadolibre.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.regex.Pattern;

public class ValidDnaSequenceValidator implements ConstraintValidator<ValidDnaSequence, List<String>> {

    private static final int MIN_N = 4;
    private static final Pattern VALID_CHARS_PATTERN = Pattern.compile("^[ATCG]+$");

    @Override
    public boolean isValid(List<String> dna, ConstraintValidatorContext context) {
        if (dna == null || dna.isEmpty()) {
            return false;
        }

        final int N = dna.size();

        if (N < MIN_N) {
            return false;
        }

        for (String row : dna) {

            if (row == null || row.length() != N) {
                return false;
            }

            if (!VALID_CHARS_PATTERN.matcher(row).matches()) {
                return false;
            }
        }

        return true;
    }
}