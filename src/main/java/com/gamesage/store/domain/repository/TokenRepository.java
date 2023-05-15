package com.gamesage.store.domain.repository;

import com.gamesage.store.domain.model.AuthToken;

import java.util.Optional;

public interface TokenRepository extends Repository<AuthToken, Integer> {

    Optional<AuthToken> findByValue(String token);

    void removeExpired();

    void removeByValue(String token);
}

