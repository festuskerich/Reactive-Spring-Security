package com.festuskerich.auth.service;

import com.festuskerich.auth.dto.ApiResponse;
import com.festuskerich.auth.dto.LoginRequest;
import com.festuskerich.auth.dto.RegisterRequest;
import com.festuskerich.auth.dto.TokenDto;
import com.festuskerich.auth.model.User;

import reactor.core.publisher.Mono;

public interface UserService {

    Mono<ApiResponse<TokenDto>> findByEmail(LoginRequest request);

    Mono<User> findByEmail(String request);

    Mono<ApiResponse<String>> register(RegisterRequest request);
}