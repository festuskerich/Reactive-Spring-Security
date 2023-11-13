package com.festuskerich.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.festuskerich.auth.dto.ApiResponse;
import com.festuskerich.auth.dto.LoginRequest;
import com.festuskerich.auth.dto.RegisterRequest;
import com.festuskerich.auth.dto.TokenDto;
import com.festuskerich.auth.service.UserService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("auth/v1/")
public class AuthApi {
    private final UserService userService;

    public AuthApi(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/token")
    public Mono<ApiResponse<TokenDto>> login(@RequestBody LoginRequest request) {
        return userService.findByEmail(request);
    }

    @PostMapping("register")
    public Mono<ApiResponse<String>> register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }

}