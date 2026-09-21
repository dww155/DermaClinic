package com.dww.DermaClinic.config;

import com.dww.DermaClinic.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${jwt.secret}")
    String signingKey;

    private static final String[] PUBLIC_ENDPOINTS = {
            "/auth/login",
            "/auth/introspect",
    };

    private static final String[] SWAGGER_ENDPOINTS = {
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/api-docs/**",
            "/api-docs",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
                    .requestMatchers(SWAGGER_ENDPOINTS).permitAll()
                    .anyRequest().authenticated())

            .oauth2ResourceServer(oauth -> oauth
                    .jwt(jwt -> jwt
                            .decoder(jwtDecoder())
                            .jwtAuthenticationConverter(jwtAuthenticationConverter())
                    )
                    // 401: missing/invalid/expired token
                    .authenticationEntryPoint((request, response, ex) -> {
                        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;
                        response.setStatus(errorCode.getStatusCode().value());
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                        Map<String, Object> body = Map.of(
                                "code",    errorCode.getCode(),
                                "message", errorCode.getMessage()
                        );

                        new ObjectMapper().writeValue(response.getWriter(), body);
                    })
            )

            // 403: authenticated but insufficient permission
            .exceptionHandling(ex -> ex
                    .accessDeniedHandler((request, response, accessDeniedException) -> {
                        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
                        response.setStatus(errorCode.getStatusCode().value());
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                        Map<String, Object> body = Map.of(
                                "code",    errorCode.getCode(),
                                "message", errorCode.getMessage()
                        );

                        new ObjectMapper().writeValue(response.getWriter(), body);
                    })
            );

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        converter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

        return converter;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(
                signingKey.getBytes(StandardCharsets.UTF_8),
                "HS256"
        );

        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .build();
    }
}
