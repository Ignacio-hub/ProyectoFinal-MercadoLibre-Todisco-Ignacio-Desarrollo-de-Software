package com.mercadolibre.examen_mercadolibre.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mutant Detector API - Examen MercadoLibre")
                        .version("1.0")
                        .description("API REST para detectar mutantes analizando secuencias de ADN. Desarrollado como examen técnico."));
    }
}