package com.mercadolibre.examen_mercadolibre.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DnaRequest {


    private List<String> dna;
}