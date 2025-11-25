package com.mercadolibre.examen_mercadolibre.Service;

import com.mercadolibre.examen_mercadolibre.service.MutantDetector;
import com.mercadolibre.examen_mercadolibre.exception.InvalidDnaSequenceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



// No usamos @SpringBootTest, es una prueba unitaria pura de la lógica
class MutantDetectorTest {

    private final MutantDetector detector = new MutantDetector();


    @Test
    @DisplayName("01. Mutante: Caso de ejemplo oficial (H y D)")
    void isMutant_CasoEjemploDocumento_ReturnsTrue() {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        assertTrue(detector.isMutant(dna), "Debe ser mutante según el ejemplo oficial.");
    }

    @Test
    @DisplayName("02. Mutante: Dos secuencias horizontales distintas (AAAA y CCCC)")
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
    @DisplayName("03. Mutante: Secuencia vertical y una diagonal descendente (TTTT y AAAA)")
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
    @DisplayName("04. Mutante: Dos secuencias oblicuas ascendentes (Diagonal invertida)")
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
    @Test
    @DisplayName("05. Mutante: Dos secuencias puramente verticales (AAAA y TTTT)")
    void isMutant_PureVertical_ReturnsTrue() {
        String[] dna = {
                "ATGC",
                "ATGC",
                "ATGC",
                "ATGC",
        };
        assertTrue(detector.isMutant(dna), "Debe detectar AAAA y TTTT verticales.");
    }
    @Test
    @DisplayName("06. Mutante: Dos secuencias en el borde derecho (Vertical y Diagonal)")
    void isMutant_AtRightBoundary_ReturnsTrue() {
        String[] dna = {
                "CCGAAA",
                "AGTTAA",
                "AAGTAA", // <--- V: AAAA en col 5
                "CCGAAA",
                "CCGAAA",
                "CCGAAA"
        };
        assertTrue(detector.isMutant(dna), "Debe detectar secuencias en el borde derecho (col 5).");
    }
    @Test
    @DisplayName("07. Mutante: Secuencias Horizontal y Vertical en la primera columna")
    void isMutant_VerticalAndHorizontalAtStart_ReturnsTrue() {
        String[] dna = {
                "AAAAGG",
                "AAGTGC",
                "AAGTGT",
                "AAGTGG",
                "CCCTTA",
                "TCACTG"
        };
        assertTrue(detector.isMutant(dna), "Debe detectar secuencias desde (0,0).");
    }
    @Test
    @DisplayName("08. Mutante: Secuencias Diagonal Descendente y Ascendente")
    void isMutant_BothDiagonals_ReturnsTrue() {
        String[] dna = {
                "ATGT",
                "CAGA",
                "TTAC",
                "AGAT"
        };
        assertTrue(detector.isMutant(dna), "Debe detectar ambas diagonales (Descendente: AAAA y Ascendente: TTTT).");
    }
    @Test
    @DisplayName("09. Humano: Sin ninguna secuencia de 4")
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

    @Test
    @DisplayName("10. Humano: Con EXACTAMENTE UNA secuencia (No es mutante)")
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

    @Test
    @DisplayName("11. Validación: Falla si una fila es de diferente tamaño (No NxN)")
    void isMutant_InputIsNotNxN_ThrowsException() {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA"}; // 6x5
        assertThrows(InvalidDnaSequenceException.class, () -> detector.isMutant(dna),
                "Debe fallar por no ser una matriz cuadrada.");
    }

    @Test
    @DisplayName("12. Validación: Falla por contener caracteres inválidos (ej: 'X')")
    void isMutant_InvalidCharacters_ThrowsException() {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTZ", "TCACTG"};
        assertThrows(InvalidDnaSequenceException.class, () -> detector.isMutant(dna),
                "Debe fallar por contener el carácter Z.");
    }
    @Test
    @DisplayName("13. Validación: Falla cuando el array de ADN es nulo")
    void isMutant_NullDna_ThrowsException() {
        assertThrows(InvalidDnaSequenceException.class, () -> detector.isMutant(null),
                "Debe fallar cuando el array de ADN es nulo (chequeo en línea 14 de MutantDetector).");
    }
    @Test
    @DisplayName("14. Validación: Falla cuando el array de ADN está vacío")
    void isMutant_EmptyDnaArray_ThrowsException() {
        String[] dna = {};
        assertThrows(InvalidDnaSequenceException.class, () -> detector.isMutant(dna),
                "Debe fallar cuando el array de ADN está vacío (chequeo en línea 14 de MutantDetector).");
    }
    @Test
    @DisplayName("15. Validación: Falla si la matriz es menor que 4x4 (N < 4)")
    void testTooSmallDna() {
        String[] dna = {
                "ATG",
                "CAG",
                "TTA"
        };
        assertThrows(InvalidDnaSequenceException.class, () -> detector.isMutant(dna),
                "Debe fallar cuando la matriz es menor que 4x4.");
    }
}