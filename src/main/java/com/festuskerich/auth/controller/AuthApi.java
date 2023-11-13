package com.festuskerich.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.festuskerich.auth.dto.ApiResponse;
import com.festuskerich.auth.dto.LoginRequest;
import com.festuskerich.auth.dto.TokenDto;
import com.festuskerich.auth.dto.RegisterRequest;
import com.festuskerich.auth.model.User;
import com.festuskerich.auth.security.Tokenizer;
import com.festuskerich.auth.service.UserService;

import reactor.core.publisher.Mono;



@RestController
@RequestMapping("auth/v1/")
public class AuthApi {

    private final Tokenizer tokenizer;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public AuthApi(Tokenizer tokenizer, PasswordEncoder passwordEncoder, UserService userService) {
        this.tokenizer = tokenizer;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("token")
    public Mono<ResponseEntity<TokenDto>> login(@RequestBody LoginRequest request) {
        // start with find requested email in DB
        return userService.findByEmail(request.getEmail())
                // match password
                .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
                // transform to user id
                .map(User::getId)
                // map as desired spec and generate token (JWT)
                .map(userId -> {
                    return ResponseEntity.ok(new TokenDto(tokenizer.tokenize(Long.toString(userId)),"",3600,""));
                })
                // fail to log in? mark as unauthorized.
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping("register")
    public Mono<ApiResponse<String>> register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }

}