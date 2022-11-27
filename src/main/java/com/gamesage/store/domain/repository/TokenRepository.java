package com.gamesage.store.domain.repository;

import com.gamesage.store.security.model.AuthToken;

import java.util.Optional;

public interface TokenRepository {

    Optional<AuthToken> findByValue(String token);

    Optional<AuthToken> findByUserLogin(String login);

    AuthToken saveToken(AuthToken AuthToken);
}

