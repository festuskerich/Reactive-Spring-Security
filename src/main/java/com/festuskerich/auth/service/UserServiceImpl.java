package com.festuskerich.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.festuskerich.auth.dto.ApiResponse;
import com.festuskerich.auth.dto.LoginRequest;
import com.festuskerich.auth.dto.RegisterRequest;
import com.festuskerich.auth.dto.TokenDto;
import com.festuskerich.auth.exception.RegisterDuplicatedEmail;
import com.festuskerich.auth.exception.UnauthorizedException;
import com.festuskerich.auth.model.User;
import com.festuskerich.auth.repository.UserRepository;
import com.festuskerich.auth.security.Tokenizer;

import java.util.function.Function;

import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Tokenizer tokenizer;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, Tokenizer tokenizer) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenizer = tokenizer;
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Mono<ApiResponse<String>> register(RegisterRequest request) {
        // find this email in DB
        return userRepository.findByEmail(request.email())
                // default as empty User of doesn't exist in DB
                .defaultIfEmpty(new User())
                // check User object before register user
                .flatMap((Function<User, Mono<User>>) user -> {
                    if (user.getId() != null) {
                        // can't register using requested email because the email is already existsin
                        // DB.
                        // so, we return empty Mono to be handled in next operation
                        return Mono.empty();
                    }
                    // new userentity
                    User entity = new User();
                    entity.setEmail(request.email());
                    entity.setUsername(request.name());
                    entity.setPassword(passwordEncoder.encode(request.password()));
                    // save entity, any error will be handled in onErrorResume
                    return userRepository.save(entity);
                })
                // in case that we can save User, return HTTP 201
                .map(user -> new ApiResponse<String>(201, "",
                        "The user " + user.getUsername() + " created successfully"))
                // in case that we got empty Mono from previous operation, which mean requested
                // email is already exists in DB.
                // so, throw an exception for duplicated email
                .switchIfEmpty(Mono.error(new RegisterDuplicatedEmail()))
                // handle any other exception like SQLException and Bad SQL Grammar Exception
                .onErrorResume(Mono::error);
    }

    @Override
    public Mono<ApiResponse<TokenDto>> findByEmail(LoginRequest request) {
        return userRepository.findByEmail(request.getEmail())
                .flatMap(user -> {
                    if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                        return Mono.just(user.getId());
                    } else {
                        return Mono.error(new UnauthorizedException("Unauthorized, uses is not found"));
                    }
                })
                .map(userId -> new TokenDto(tokenizer.tokenize(Long.toString(userId)), "Bearer", 3600,
                        "Token"))
                .map(tokenDto -> new ApiResponse<>(0, "Success", tokenDto))
                .defaultIfEmpty(new ApiResponse<>(00, "User not found", null))
                .onErrorResume(UnauthorizedException.class,
                        ex -> Mono.just(new ApiResponse<>(401, "Unauthorized", null)));
    }

}
