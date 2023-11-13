package com.festuskerich.auth.exception;

public class RegisterDuplicatedEmail extends RuntimeException {
    public RegisterDuplicatedEmail() {
        super("Duplicated email in registration");
    }
}
