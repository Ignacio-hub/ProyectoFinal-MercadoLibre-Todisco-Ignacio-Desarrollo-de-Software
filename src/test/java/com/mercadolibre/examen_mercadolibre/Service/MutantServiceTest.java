package com.mercadolibre.examen_mercadolibre.Service;

import com.mercadolibre.examen_mercadolibre.service.MutantDetector;
import com.mercadolibre.examen_mercadolibre.entity.DnaRecord;
import com.mercadolibre.examen_mercadolibre.repository.DnaRecordRepository;
import com.mercadolibre.examen_mercadolibre.service.MutantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MutantServiceTest {

    @Mock
    private MutantDetector mutantDetector;

    @Mock
    private DnaRecordRepository dnaRecordRepository;

    @InjectMocks
    private MutantService mutantService;

    private String[] mutantDna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
    private String[] humanDna = {"ATGCGA", "AAGTGC", "TTATTT", "AGACGG", "GTCCTA", "TCACTG"};
    private DnaRecord mutantRecord = new DnaRecord("hashMutant", true, "sequence");
    private DnaRecord humanRecord = new DnaRecord("hashHuman", false, "sequence");


    @BeforeEach
    void setUp() {
        // Reiniciamos mocks o configuraciones si es necesario
    }

    // ==============================================================================
    // CASOS SIN CACHÉ (Cache Miss) - Debe calcular y guardar
    // ==============================================================================

    @Test
    @DisplayName("Debe detectar mutante, llamar al detector y guardar el registro (Cache Miss)")
    void isMutant_NoCacheHit_IsMutant_ShouldCallDetectorAndSave() {
        // Arrange
        when(dnaRecordRepository.existsByDnaHash(anyString())).thenReturn(false); // No está en caché
        when(mutantDetector.isMutant(mutantDna)).thenReturn(true); // Detector dice que SÍ es mutante

        // Act
        boolean result = mutantService.isMutant(mutantDna);

        // Assert
        assertTrue(result);
        // Debe verificar si existe (false)
        verify(dnaRecordRepository, times(1)).existsByDnaHash(anyString());
        // Debe llamar al detector una vez
        verify(mutantDetector, times(1)).isMutant(mutantDna);
        // Debe guardar el resultado una vez
        verify(dnaRecordRepository, times(1)).save(any(DnaRecord.class));
    }

    @Test
    @DisplayName("Debe detectar humano, llamar al detector y guardar el registro (Cache Miss)")
    void isMutant_NoCacheHit_IsHuman_ShouldCallDetectorAndSave() {
        // Arrange
        when(dnaRecordRepository.existsByDnaHash(anyString())).thenReturn(false); // No está en caché
        when(mutantDetector.isMutant(humanDna)).thenReturn(false); // Detector dice que NO es mutante

        // Act
        boolean result = mutantService.isMutant(humanDna);

        // Assert
        assertFalse(result);
        // Debe llamar al detector una vez
        verify(mutantDetector, times(1)).isMutant(humanDna);
        // Debe guardar el resultado una vez
        verify(dnaRecordRepository, times(1)).save(any(DnaRecord.class));
    }

    // ==============================================================================
    // CASOS CON CACHÉ (Cache Hit) - No debe calcular ni guardar
    // ==============================================================================

    @Test
    @DisplayName("Debe retornar True por caché (Cache Hit - Mutante)")
    void isMutant_CacheHit_IsMutant_ShouldReturnCachedResult() {
        // Arrange
        when(dnaRecordRepository.existsByDnaHash(anyString())).thenReturn(true); // Está en caché
        when(dnaRecordRepository.findById(anyString())).thenReturn(Optional.of(mutantRecord));

        // Act
        boolean result = mutantService.isMutant(mutantDna);

        // Assert
        assertTrue(result);
        // Debe verificar si existe (true)
        verify(dnaRecordRepository, times(1)).existsByDnaHash(anyString());
        // Debe buscar por ID
        verify(dnaRecordRepository, times(1)).findById(anyString());
        // NO debe llamar al detector
        verify(mutantDetector, never()).isMutant(any());
        // NO debe guardar
        verify(dnaRecordRepository, never()).save(any(DnaRecord.class));
    }

    @Test
    @DisplayName("Debe retornar False por caché (Cache Hit - Humano)")
    void isMutant_CacheHit_IsHuman_ShouldReturnCachedResult() {
        // Arrange
        when(dnaRecordRepository.existsByDnaHash(anyString())).thenReturn(true); // Está en caché
        when(dnaRecordRepository.findById(anyString())).thenReturn(Optional.of(humanRecord));

        // Act
        boolean result = mutantService.isMutant(humanDna);

        // Assert
        assertFalse(result);
        // Debe buscar por ID
        verify(dnaRecordRepository, times(1)).findById(anyString());
        // NO debe llamar al detector
        verify(mutantDetector, never()).isMutant(any());
        // NO debe guardar
        verify(dnaRecordRepository, never()).save(any(DnaRecord.class));
    }
}