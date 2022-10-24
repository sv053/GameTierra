package com.gamesage.store.domain.repository;

import com.gamesage.store.security.model.AuthToken;

import java.util.Optional;

public interface TokenRepository {

    Optional<AuthToken> retrieveByUserId(int userId);

    Optional<AuthToken> retrieveByValue(String token);

    AuthToken persistToken(int userId);
}

