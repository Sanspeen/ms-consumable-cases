package com.example.api.mock.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mock API - Customers / Products / Orders")
                        .version("1.0.0")
                        .description("API mock para practicar consumo de endpoints y respuestas HTTP"));
    }
}