package com.mercadolibre.examen_mercadolibre.service;

import com.mercadolibre.examen_mercadolibre.entity.DnaRecord;
import com.mercadolibre.examen_mercadolibre.repository.DnaRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class MutantService {

    private final MutantDetector mutantDetector;
    private final DnaRecordRepository dnaRecordRepository;

    public boolean isMutant(String[] dna) {

        String dnaSequence = Arrays.toString(dna);
        String dnaHash = calculateHash(dnaSequence);


        if (dnaRecordRepository.existsByDnaHash(dnaHash)) {

            return dnaRecordRepository.findById(dnaHash)
                    .map(DnaRecord::isMutant)
                    .orElse(false);
        }

        boolean isMutantResult = mutantDetector.isMutant(dna);

        DnaRecord record = new DnaRecord(dnaHash, isMutantResult, dnaSequence);
        dnaRecordRepository.save(record);

        return isMutantResult;
    }

    private String calculateHash(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {

            throw new RuntimeException("Error al calcular el hash", e);
        }
    }
}
