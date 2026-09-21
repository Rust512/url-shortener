package com.training.url_shortener.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocumentationConfig {

    private static final String INSTRUCTIONS = """
            ### URL shortening service
            
            Use the POST /v1/api/shorten API to shorten a URL.
            This app uses Nano IDs to generate a cryptographically random ID to your long URL!
            
            Accessing /{id} URL will redirect to the URL registered against it.
            """;

    @Bean
    OpenAPI customOpenAPI() {
        var contactInfo = new Contact()
                .name("Devang Hemant Bhagwat")
                .email("dbhagwat512@gmail.com");
        var information = new Info()
                .title("URL Shortener")
                .version("1.0")
                .description(INSTRUCTIONS)
                .contact(contactInfo);
        return new OpenAPI()
                .info(information);
    }
}