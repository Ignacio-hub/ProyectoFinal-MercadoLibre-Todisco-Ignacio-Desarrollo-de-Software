package com.mercadolibre.examen_mercadolibre.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatsResponse {

    @JsonProperty("count_mutant_dna")
    @Schema(description = "Cantidad de ADN mutante verificado.")
    private long countMutantDna;

    @JsonProperty("count_human_dna")
    @Schema(description = "Cantidad de ADN humano verificado.")
    private long countHumanDna;

    @Schema(description = "Relación entre mutantes y humanos (mutantes / humanos).")
    private double ratio;
}
