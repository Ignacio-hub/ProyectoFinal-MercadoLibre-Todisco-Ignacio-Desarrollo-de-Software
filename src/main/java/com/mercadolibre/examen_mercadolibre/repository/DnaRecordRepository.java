package com.mercadolibre.examen_mercadolibre.repository;

import com.mercadolibre.examen_mercadolibre.entity.DnaRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DnaRecordRepository extends JpaRepository<DnaRecord, String> {
    long countByIsMutantTrue();
    long countByIsMutantFalse();
    boolean existsByDnaHash(String dnaHash);
}