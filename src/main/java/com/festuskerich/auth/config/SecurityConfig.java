package com.festuskerich.auth.config;

import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {

    private static final String[] PUBLIC = { "/", "/favicon.ico", "/actuator/health", "/auth/v1/token",
            "/auth/v1/register" };

    private final TokenSecurityContextRepository securityContextRepository;

    public SecurityConfig(TokenSecurityContextRepository securityContextRepository) {
        this.securityContextRepository = securityContextRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(csrf -> csrf
                        .disable()
                        .formLogin(login -> login
                                .disable()
                                .httpBasic(basic -> basic
                                        .disable()
                                        .securityContextRepository(securityContextRepository)
                                        .authorizeExchange(exchange -> exchange
                                                .pathMatchers(PUBLIC)
                                                .permitAll()
                                                .anyExchange()
                                                .authenticated()))))
                .build();
    }

}
