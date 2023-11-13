package com.festuskerich.auth.config;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.festuskerich.auth.security.Tokenizer;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Component
public class TokenAuthenticationManager implements ReactiveAuthenticationManager {

    private final Tokenizer tokenizer;

    public TokenAuthenticationManager(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.justOrEmpty(authentication.getCredentials())
                .filter(Objects::nonNull)
                .flatMap((Function<Object, Mono<DecodedJWT>>) credential -> tokenizer.verify((String) credential))
                .flatMap((Function<DecodedJWT, Mono<UsernamePasswordAuthenticationToken>>) decodedJWT -> {
                    String userId = decodedJWT.getClaim("principal").asString();
                    String role = decodedJWT.getClaim("role").asString();
                    List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
                    return Mono.just(new UsernamePasswordAuthenticationToken(userId, null, authorities));
                });
    }

}