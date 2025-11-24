package com.mercadolibre.examen_mercadolibre.Service;

import com.mercadolibre.examen_mercadolibre.dto.StatsResponse;
import com.mercadolibre.examen_mercadolibre.repository.DnaRecordRepository;
import com.mercadolibre.examen_mercadolibre.service.StatsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {

    @Mock
    private DnaRecordRepository dnaRecordRepository;

    @InjectMocks
    private StatsService statsService;

    // ==============================================================================
    // CASOS DE RATIO
    // ==============================================================================

    @Test
    @DisplayName("Debe calcular el ratio correctamente (10 mutantes / 100 humanos = 0.1)")
    void getDnaStats_StandardCalculation_ShouldReturnCorrectRatio() {
        // Arrange
        long mutantCount = 10;
        long humanCount = 100;
        when(dnaRecordRepository.countByIsMutantTrue()).thenReturn(mutantCount);
        when(dnaRecordRepository.countByIsMutantFalse()).thenReturn(humanCount);
        double expectedRatio = 0.1;

        // Act
        StatsResponse response = statsService.getDnaStats();

        // Assert
        assertEquals(mutantCount, response.getCountMutantDna());
        assertEquals(humanCount, response.getCountHumanDna());
        assertEquals(expectedRatio, response.getRatio(), 0.0001); // El delta permite errores de punto flotante
    }

    @Test
    @DisplayName("Debe calcular el ratio correctamente (50 mutantes / 50 humanos = 1.0)")
    void getDnaStats_RatioOne_ShouldReturnOne() {
        // Arrange
        long count = 50;
        when(dnaRecordRepository.countByIsMutantTrue()).thenReturn(count);
        when(dnaRecordRepository.countByIsMutantFalse()).thenReturn(count);
        double expectedRatio = 1.0;

        // Act
        StatsResponse response = statsService.getDnaStats();

        // Assert
        assertEquals(expectedRatio, response.getRatio(), 0.0001);
    }

    @Test
    @DisplayName("Debe manejar la división por cero (0 humanos) retornando 0.0")
    void getDnaStats_ZeroHumans_ShouldReturnZero() {
        // Arrange
        long mutantCount = 10;
        long humanCount = 0;
        when(dnaRecordRepository.countByIsMutantTrue()).thenReturn(mutantCount);
        when(dnaRecordRepository.countByIsMutantFalse()).thenReturn(humanCount);
        double expectedRatio = 0.0;

        // Act
        StatsResponse response = statsService.getDnaStats();

        // Assert
        assertEquals(expectedRatio, response.getRatio(), 0.0001);
        assertEquals(mutantCount, response.getCountMutantDna());
        assertEquals(humanCount, response.getCountHumanDna());
    }

    @Test
    @DisplayName("Debe retornar 0.0 cuando hay 0 mutantes y 100 humanos")
    void getDnaStats_ZeroMutants_ShouldReturnZero() {
        // Arrange
        long mutantCount = 0;
        long humanCount = 100;
        when(dnaRecordRepository.countByIsMutantTrue()).thenReturn(mutantCount);
        when(dnaRecordRepository.countByIsMutantFalse()).thenReturn(humanCount);
        double expectedRatio = 0.0;

        // Act
        StatsResponse response = statsService.getDnaStats();

        // Assert
        assertEquals(expectedRatio, response.getRatio(), 0.0001);
    }
}