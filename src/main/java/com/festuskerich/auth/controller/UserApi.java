package com.festuskerich.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Function;

@RestController
@RequestMapping("user")
public class UserApi {

    //private final UserRepository userRepository;

    //public UserApi(UserRepository userRepository) {
     //   this.userRepository = userRepository;
    //}

    // @GetMapping
    // public Mono<ResponseEntity<User>> getProfile(Authentication authentication) {
    //     return Mono.justOrEmpty(authentication)

    //             .filter(Objects::nonNull)

    //             .switchIfEmpty(Mono.error(UserException.unauthorized()))

    //             .map(auth -> (String) auth.getPrincipal())

    //             .flatMap((Function<String, Mono<User>>) userId -> userRepository.findById(Long.parseLong(userId)))

    //             .map(user -> {
    //                 MUser model = new MUser();
    //                 model.setEmail(user.getEmail());
    //                 model.setName(user.getName());
    //                 return model;
    //             })

    //             .switchIfEmpty(Mono.error(UserException.unauthorized()))

    //             .map(ResponseEntity::ok);
    // }

}