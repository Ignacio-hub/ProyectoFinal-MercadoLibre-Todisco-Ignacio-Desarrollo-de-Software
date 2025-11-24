package com.mercadolibre.examen_mercadolibre.Service;

import com.mercadolibre.examen_mercadolibre.service.MutantDetector;
import com.mercadolibre.examen_mercadolibre.exception.InvalidDnaSequenceException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



// No usamos @SpringBootTest, es una prueba unitaria pura de la lógica
class MutantDetectorTest {

    private final MutantDetector detector = new MutantDetector();


    @Test
    void isMutant_CasoEjemploDocumento_ReturnsTrue() {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        assertTrue(detector.isMutant(dna), "Debe ser mutante según el ejemplo oficial.");
    }

    // Mutante por dos secuencias horizontales distintas
    @Test
    void isMutant_TwoHorizontalSequences_ReturnsTrue() {
        String[] dna = {
                "AAAAAG",
                "GCGTCA",
                "CCCCGT",
                "AGTCGT",
                "TTTTTT",
                "TCACTA"
        };
        assertTrue(detector.isMutant(dna), "Debe detectar AAAA y CCCC.");
    }

    // Mutante por una secuencia Vertical y una Oblicua
    @Test
    void isMutant_VerticalAndDiagonalRight_ReturnsTrue() {
        String[] dna = {
                "TCGCGA",
                "TGAATG",
                "TCCATA",
                "TGGGTA",
                "TTCTTA",
                "TCACTG"
        };
        assertTrue(detector.isMutant(dna), "Debe detectar TTTT vertical y la diagonal.");
    }

    // Mutante por dos secuencias Oblicuas Izquierdas (/)
    @Test
    void isMutant_TwoDiagonalLeft_ReturnsTrue() {
        String[] dna = {
                "ATCCCG",
                "GTCTCC",
                "CTTCGC",
                "GTAGTA",
                "GGAGGG",
                "GACTGA"
        };
        // Busca secuencias como TTTT y CCCC en diagonal invertida
        assertTrue(detector.isMutant(dna), "Debe detectar al menos dos diagonales invertidas.");
    }

    // ==========================================================
    // CASOS HUMANOS (Debe devolver FALSE)
    // ==========================================================

    // Caso humano base
    @Test
    void isMutant_HumanDna_ReturnsFalse() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATTT",
                "AGACGG",
                "GTCCTA",
                "TCACTG"
        };
        assertFalse(detector.isMutant(dna), "No hay secuencias de 4.");
    }

    // Caso con EXACTAMENTE UNA secuencia (No es mutante)
    @Test
    void isMutant_OneSequenceOnly_ReturnsFalse() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTTTGT", // SOLO una secuencia
                "AGACGG",
                "GTCCTA",
                "TCACTG"
        };
        assertFalse(detector.isMutant(dna), "Debe ser False con solo UNA secuencia.");
    }

    // ==========================================================
    // CASOS DE EXCEPCIÓN (Validación de Input)
    // ==========================================================

    // Caso: No es NxN (Rectangular)
    @Test
    void isMutant_InputIsNotNxN_ThrowsException() {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA"}; // 6x5
        assertThrows(InvalidDnaSequenceException.class, () -> detector.isMutant(dna),
                "Debe fallar por no ser una matriz cuadrada.");
    }

    // Caso: Caracteres inválidos
    @Test
    void isMutant_InvalidCharacters_ThrowsException() {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTZ", "TCACTG"};
        assertThrows(InvalidDnaSequenceException.class, () -> detector.isMutant(dna),
                "Debe fallar por contener el carácter Z.");
    }
}