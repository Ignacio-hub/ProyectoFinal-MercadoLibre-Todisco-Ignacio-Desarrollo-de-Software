package com.mercadolibre.examen_mercadolibre.dto;

import com.mercadolibre.examen_mercadolibre.validation.ValidDnaSequence;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DnaRequest {

    @Schema(description = "Secuencia de ADN representada como un array de Strings (matriz NxN) con bases A, T, C, G.",
            example = "[\"ATGCGA\", \"CAGTGC\", \"TTATGT\", \"AGAAGG\", \"CCCCTA\", \"TCACTG\"]")
    @NotNull(message = "El campo 'dna' no puede ser nulo.")
    @NotEmpty(message = "La secuencia de ADN no puede estar vacía.")
    @ValidDnaSequence
    private List<String> dna;
}