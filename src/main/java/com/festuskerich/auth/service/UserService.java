package com.festuskerich.auth.service;

import com.festuskerich.auth.dto.ApiResponse;
import com.festuskerich.auth.dto.RegisterRequest;
import com.festuskerich.auth.model.User;

import reactor.core.publisher.Mono;

public interface UserService {

    Mono<User> findByEmail(String email);
    Mono<ApiResponse<String>> register(RegisterRequest request);  
} 