package com.marcmeola.frases.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Disable CSRF for API
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 1. GET: Authors and Standard users can read
                        .requestMatchers(HttpMethod.GET, Config.API_URL + "/**").hasAnyRole("STANDARD", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/web/**").hasAnyRole("STANDARD", "ADMIN")

                        // Allow voting for STANDARD users too (User extension)
                        .requestMatchers(HttpMethod.POST, Config.API_URL + "/frases/*/valoraciones")
                        .hasAnyRole("STANDARD", "ADMIN")

                        // 2. PUT/PATCH/POST/DELETE/Etc: Only ADMIN
                        .requestMatchers(Config.API_URL + "/**").hasRole("ADMIN")

                        // Swagger UI open
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // Any other request must be authenticated
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults()); // Basic Auth
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
