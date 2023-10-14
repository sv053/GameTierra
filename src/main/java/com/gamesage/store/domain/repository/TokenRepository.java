package com.gamesage.store.domain.repository;

import com.gamesage.store.domain.model.AuthToken;

import java.util.Optional;

public interface TokenRepository extends Repository<AuthToken, Integer> {

    Optional<AuthToken> findByUserId(Integer id);

    Optional<AuthToken> findById(Integer id);

    AuthToken updateByUserId(AuthToken token, Integer userId);

    int removeExpired();

    boolean removeByUserId(Integer id);
}

