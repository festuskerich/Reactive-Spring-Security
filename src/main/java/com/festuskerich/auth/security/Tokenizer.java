package com.festuskerich.auth.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Calendar;
import java.util.Date;

@Component
public class Tokenizer {

    @Value("${app.token.secret}")
    private String secret;

    @Value("${app.token.issuer}")
    private String issuer;

    @Value("${app.token.expires-minute}")
    private int expires;

    public String tokenize(String userId) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expires);
        Date expiresAt = calendar.getTime();
        return JWT.create()
                .withIssuer(issuer)
                .withClaim("principal", userId)
                .withClaim("role", "USER")
                .withExpiresAt(expiresAt)
                .sign(algorithm());
    }

    public Mono<DecodedJWT> verify(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm()).withIssuer(issuer).build();
            return Mono.just(verifier.verify(token));
        } catch (Exception e) {
            return Mono.empty();
        }
    }

    private Algorithm algorithm() {
        return Algorithm.HMAC256(secret);
    }

}