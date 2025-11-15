package com.mercadolibre.examen_mercadolibre.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "dna_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DnaRecord {

    @Id
    @Column(name = "dna_hash", nullable = false, unique = true)
    private String dnaHash;

    @Column(name = "is_mutant", nullable = false)
    private boolean isMutant;

    @Column(name = "dna_sequence", length = 5000)
    private String dnaSequence;
}