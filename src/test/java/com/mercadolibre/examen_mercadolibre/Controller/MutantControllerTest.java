package com.mercadolibre.examen_mercadolibre.Controller;

import com.mercadolibre.examen_mercadolibre.repository.DnaRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

// Importaciones estáticas para facilitar la lectura de los tests
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat; // AssertJ
import static org.hamcrest.Matchers.is;
@SpringBootTest
@AutoConfigureMockMvc
class MutantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DnaRecordRepository repository;

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }


    @Test
    void postMutant_isMutant_Returns200AndSavesRecord() throws Exception {

        String mutantDna = "{\"dna\":[\"ATGCGA\",\"CAGTGC\",\"TTATGT\",\"AGAAGG\",\"CCCCTA\",\"TCACTG\"]}";

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mutantDna))
                .andExpect(status().isOk());

        assertThat(repository.count()).isEqualTo(1L);
        assertThat(repository.countByIsMutantTrue()).isEqualTo(1L);
    }
    @Test
    void postMutant_InvalidCharacters_Returns400BadRequest() throws Exception {

        String invalidCharDna = "{\"dna\":[\"ATGC\",\"CAGT\",\"TTAT\",\"AGAX\"]}";

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidCharDna))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request - DNA Validation Error")));


        assertThat(repository.count()).isEqualTo(0L);
    }
    @Test
    void postMutant_InvalidStructure_Returns400BadRequest() throws Exception {

        String nonNxNDna = "{\"dna\":[\"ATGC\", \"CAG\", \"TTAT\", \"AGAC\"]}";

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nonNxNDna))
                .andExpect(status().isBadRequest()) // Verifica 400 Bad Request
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request - DNA Validation Error")));


        assertThat(repository.count()).isEqualTo(0L);
    }

    @Test
    void postMutant_isHuman_Returns403AndSavesRecord() throws Exception {

        String humanDna = "{\"dna\":[\"ATGCGA\",\"AAGTGC\",\"TTATTT\",\"AGACGG\",\"GTCCTA\",\"TCACTG\"]}";

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(humanDna))
                .andExpect(status().isForbidden());

        assertThat(repository.count()).isEqualTo(1L);
        assertThat(repository.countByIsMutantFalse()).isEqualTo(1L);
    }

    @Test
    void postMutant_DuplicateDna_DoesNotSaveTwice() throws Exception {

        String mutantDna = "{\"dna\":[\"ATGCGA\",\"CAGTGC\",\"TTATGT\",\"AGAAGG\",\"CCCCTA\",\"TCACTG\"]}";

        mockMvc.perform(post("/mutant").contentType(MediaType.APPLICATION_JSON).content(mutantDna));

        mockMvc.perform(post("/mutant").contentType(MediaType.APPLICATION_JSON).content(mutantDna));

        assertThat(repository.count()).isEqualTo(1L);
    }

    @Test
    void getStats_CorrectRatioCalculation() throws Exception {

        String mutantDna1 = "{\"dna\":[\"ATGCGA\",\"CAGTGC\",\"TTATGT\",\"AGAAGG\",\"CCCCTA\",\"TCACTG\"]}";
        mockMvc.perform(post("/mutant").contentType(MediaType.APPLICATION_JSON).content(mutantDna1));

        String humanDna1 = "{\"dna\":[\"ATGCGA\",\"AAGTGC\",\"TTATTT\",\"AGACGG\",\"GTCCTA\",\"TCACTG\"]}";
        String humanDna2 = "{\"dna\":[\"GTGCAA\",\"CATGAA\",\"ATGAAT\",\"CCGTCA\",\"TTGCCA\",\"GCTGTC\"]}";
        mockMvc.perform(post("/mutant").contentType(MediaType.APPLICATION_JSON).content(humanDna1));
        mockMvc.perform(post("/mutant").contentType(MediaType.APPLICATION_JSON).content(humanDna2));

        mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_mutant_dna", is(1)))
                .andExpect(jsonPath("$.count_human_dna", is(2)))
                .andExpect(jsonPath("$.ratio", is(0.5)));
    }

    @Test
    void postMutant_SmallMatrixIsMutant_Returns200AndSaves() throws Exception {
        // Dos secuencias horizontales en matriz 4x4
        String smallMutantDna = "{\"dna\":[\"AAAA\",\"CTGA\",\"GGGG\",\"TCAA\"]}";

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(smallMutantDna))
                .andExpect(status().isOk());

        assertThat(repository.countByIsMutantTrue()).isEqualTo(1L);
    }

    @Test
    void postMutant_SmallMatrixIsHuman_Returns403AndSaves() throws Exception {

        String smallHumanDna = "{\"dna\":[\"ATGC\",\"ATGA\",\"ATGC\",\"TCAA\"]}";

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(smallHumanDna))
                .andExpect(status().isForbidden());

        assertThat(repository.count()).isEqualTo(1L);
    }

    @Test
    void getStats_EmptyDatabase_ReturnsZeros() throws Exception {


        mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_mutant_dna", is(0)))
                .andExpect(jsonPath("$.count_human_dna", is(0)))
                .andExpect(jsonPath("$.ratio", is(0.0)));
    }

    @Test
    void getStats_OnlyMutantsNoHumans_ReturnsValidRatio() throws Exception {

        String mutantDna1 = "{\"dna\":[\"ATGCGA\",\"CAGTGC\",\"TTATGT\",\"AGAAGG\",\"CCCCTA\",\"TCACTG\"]}";
        mockMvc.perform(post("/mutant").contentType(MediaType.APPLICATION_JSON).content(mutantDna1));

        String mutantDna2 = "{\"dna\":[\"ATAAAA\",\"AATGCC\",\"ATATGT\",\"AAAAGG\",\"CCCCTA\",\"TCACTG\"]}";
        mockMvc.perform(post("/mutant").contentType(MediaType.APPLICATION_JSON).content(mutantDna2));

        mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_mutant_dna", is(2)))
                .andExpect(jsonPath("$.count_human_dna", is(0)))
                .andExpect(jsonPath("$.ratio", is(0.0)));
    }
}