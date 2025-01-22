package org.ikrotsyuk.mdsn.booktrackerservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.authorizeHttpRequests(
                        authManager -> authManager
                                .requestMatchers("book-tracker/swagger-ui.html", "book-tracker/swagger-ui/**", "book-tracker/v3/api-docs/**")
                                .permitAll()
                                .anyRequest().authenticated())
                .build();
    }
}