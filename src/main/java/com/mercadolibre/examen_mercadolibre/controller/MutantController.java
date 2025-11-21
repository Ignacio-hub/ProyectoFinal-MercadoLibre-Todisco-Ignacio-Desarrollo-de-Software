package com.mercadolibre.examen_mercadolibre.controller;


import com.mercadolibre.examen_mercadolibre.dto.DnaRequest;
import com.mercadolibre.examen_mercadolibre.dto.StatsResponse;
import com.mercadolibre.examen_mercadolibre.service.MutantService;
import com.mercadolibre.examen_mercadolibre.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@Tag(name = "Detector de Mutantes", description = "API para verificar si un ADN pertenece a un mutante o un humano.")
public class MutantController {

    private final MutantService mutantService;
    private final StatsService statsService;

    @Autowired
    public MutantController(MutantService mutantService, StatsService statsService) {
        this.mutantService = mutantService;
        this.statsService = statsService;
    }

    @Operation(summary = "Verificar si un ADN es mutante",
            description = "Analiza una secuencia de ADN. Retorna 200 si es mutante (más de 1 secuencia de 4 letras iguales) o 403 si es humano (0 o 1 secuencia).") // <-- NUEVO
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ADN analizado: ES MUTANTE (más de una secuencia)"),
            @ApiResponse(responseCode = "403", description = "ADN analizado: NO ES MUTANTE (cero o una secuencia)"),
            @ApiResponse(responseCode = "400", description = "Error: Secuencia de ADN inválida (no es NxN o contiene caracteres no permitidos)", content = @Content)
    })  @PostMapping("/mutant")
    public ResponseEntity<Void> checkMutant(@Validated @RequestBody DnaRequest request) {

        String[] dnaArray = request.getDna().toArray(new String[0]);

        boolean isMutant = mutantService.isMutant(dnaArray);

        if (isMutant) {
            return new ResponseEntity<>(HttpStatus.OK); // 200 OK
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // 403 Forbidden
        }
     }
@Operation(summary = "Obtener estadísticas de ADN",
            description = "Retorna el número de verificaciones de mutantes, humanos y el ratio (mutantes/humanos).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas correctamente")
    })
    @GetMapping("/stats")
    public ResponseEntity<StatsResponse> getStats() {
        StatsResponse stats = statsService.getDnaStats();
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
}