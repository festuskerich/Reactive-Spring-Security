package com.festuskerich.auth.config;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class TokenSecurityContextRepository implements ServerSecurityContextRepository {

    private final TokenAuthenticationManager manager;

    public TokenSecurityContextRepository(TokenAuthenticationManager authManager) {
        this.manager = authManager;
    }

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .filter(header -> header.startsWith("Bearer "))
                .flatMap(header -> {
                    final String token = header.substring(7);
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(token, token);
                    return this.manager.authenticate(auth).map(SecurityContextImpl::new);
                });
    }

}