package org.springframework.samples.petclinic.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for OpenAPI/Swagger documentation.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI petClinicOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("PetClinic Statistics API")
                .description("REST API for retrieving pet statistics including pet distribution by type and top requested services")
                .version("1.0.0")
                .contact(new Contact()
                    .name("PetClinic Team")
                    .email("support@petclinic.com")));
    }
}
