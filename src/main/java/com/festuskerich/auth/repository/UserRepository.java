package com.festuskerich.auth.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.festuskerich.auth.model.User;

import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<User, Long> {
   
    //@Query("select * from users where email = :email;")
    Mono<User> findByEmail(String email);
}
