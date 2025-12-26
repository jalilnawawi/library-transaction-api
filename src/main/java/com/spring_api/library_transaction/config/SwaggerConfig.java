package com.spring_api.library_transaction.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI springAppOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Library Book Transaction API")
                        .description("RESTful API services for managing library book transactions.")
                        .version("v0.0.1")
                );
    }
}
