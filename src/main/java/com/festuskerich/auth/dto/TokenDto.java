package com.festuskerich.auth.dto;

public record TokenDto(
    String access_token,
    String token_type,
    Integer expires_in, 
    String scope
){}
