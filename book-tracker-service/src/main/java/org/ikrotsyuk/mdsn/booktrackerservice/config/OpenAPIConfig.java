package org.ikrotsyuk.mdsn.booktrackerservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@OpenAPIDefinition
@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(
                        new Server().url("http://localhost:8083"),
                        new Server().url("http://localhost:8084")))
                .info(new Info().title("Book Tracker Service API").version("1.0.0"))
                .addSecurityItem(new SecurityRequirement().addList("JWTAuthScheme"))
                .components(new Components().addSecuritySchemes("JWTAuthScheme", new SecurityScheme()
                        .name("Authorization via bearer token(JWT)").type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }
}