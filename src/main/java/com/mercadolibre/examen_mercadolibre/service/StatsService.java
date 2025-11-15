package com.mercadolibre.examen_mercadolibre.service;

import com.mercadolibre.examen_mercadolibre.model.StatsResponse;
import com.mercadolibre.examen_mercadolibre.repository.DnaRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatsService {

    private final DnaRecordRepository dnaRecordRepository;

    @Autowired
    public StatsService(DnaRecordRepository dnaRecordRepository) {
        this.dnaRecordRepository = dnaRecordRepository;
    }

    public StatsResponse getDnaStats() {

        long mutantCount = dnaRecordRepository.countByIsMutantTrue();
        long humanCount = dnaRecordRepository.countByIsMutantFalse();

        double ratio = 0.0;

        if (humanCount > 0) {
            ratio = (double) mutantCount / humanCount;
        }

        return new StatsResponse(mutantCount, humanCount, ratio);
    }
}