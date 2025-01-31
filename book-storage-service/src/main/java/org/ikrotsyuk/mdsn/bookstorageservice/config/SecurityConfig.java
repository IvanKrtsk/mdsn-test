package org.ikrotsyuk.mdsn.bookstorageservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.authorizeHttpRequests(
                authManager -> authManager
                        .requestMatchers("book-storage/swagger-ui.html", "book-storage/swagger-ui/**", "book-storage/v3/api-docs/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST,"/book-storage/add").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PATCH,"/book-storage/update/*").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.DELETE,"/book-storage/delete/*").hasRole("MANAGER")
                        .anyRequest().authenticated())
        .build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        converter.setPrincipalClaimName("preferred_username");
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Collection<GrantedAuthority> authorities = jwtGrantedAuthoritiesConverter.convert(jwt);
            List<String> roles = (List<String>)jwt.getClaimAsMap("realm_access").get("roles");

            return Stream.concat(authorities.stream(),
                    roles.stream()
                            .filter(role -> role.startsWith("ROLE_"))
                            .map(SimpleGrantedAuthority::new)
                            .map(GrantedAuthority.class::cast))
                    .toList();
        });
        return converter;
    }
}
