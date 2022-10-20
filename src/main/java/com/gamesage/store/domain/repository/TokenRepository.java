package com.gamesage.store.domain.repository;

import com.gamesage.store.security.model.AuthToken;

import java.util.Optional;

public interface TokenRepository {

    Optional<AuthToken> findToken(int userId);

    Optional<AuthToken> findToken(String token);

    AuthToken createToken(int userId);
}

