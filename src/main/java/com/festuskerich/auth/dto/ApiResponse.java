package com.festuskerich.auth.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(int status, String message, T data) {
    public ApiResponse {
        Objects.requireNonNull(message, "message must not be null");
    }
}
