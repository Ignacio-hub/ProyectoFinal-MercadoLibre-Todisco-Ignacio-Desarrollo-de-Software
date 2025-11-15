package com.mercadolibre.examen_mercadolibre.controller;

import com.mercadolibre.examen_mercadolibre.model.DnaRequest;
import com.mercadolibre.examen_mercadolibre.model.StatsResponse;
import com.mercadolibre.examen_mercadolibre.service.MutantService;
import com.mercadolibre.examen_mercadolibre.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class MutantController {

    private final MutantService mutantService;
    private final StatsService statsService;

    @Autowired
    public MutantController(MutantService mutantService, StatsService statsService) {
        this.mutantService = mutantService;
        this.statsService = statsService;
    }

    @PostMapping("/mutant")
    public ResponseEntity<Void> checkMutant(@RequestBody DnaRequest request) {

        String[] dnaArray = request.getDna().toArray(new String[0]);

        boolean isMutant = mutantService.isMutant(dnaArray);

        if (isMutant) {
            return new ResponseEntity<>(HttpStatus.OK); // 200 OK
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // 403 Forbidden
        }
    }


    @GetMapping("/stats")
    public ResponseEntity<StatsResponse> getStats() {
        StatsResponse stats = statsService.getDnaStats();
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
}