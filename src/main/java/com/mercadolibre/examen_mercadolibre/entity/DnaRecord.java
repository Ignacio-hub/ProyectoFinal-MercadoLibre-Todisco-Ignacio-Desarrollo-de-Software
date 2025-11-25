package com.mercadolibre.examen_mercadolibre.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "dna_record")
@Data
@NoArgsConstructor

public class DnaRecord {

    @Id
    @Column(name = "dna_hash", nullable = false, unique = true)
    private String dnaHash;

    @Column(name = "is_mutant", nullable = false)
    private boolean isMutant;

    @Column(name = "dna_sequence", length = 5000)
    private String dnaSequence;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public DnaRecord(String dnaHash, boolean isMutant, String dnaSequence) {
        this.dnaHash = dnaHash;
        this.isMutant = isMutant;
        this.dnaSequence = dnaSequence;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }
}