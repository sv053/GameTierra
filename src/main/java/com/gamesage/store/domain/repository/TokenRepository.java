package com.gamesage.store.domain.repository;

import com.gamesage.store.domain.repository.model.AuthTokenEntity;

import java.util.Optional;

public interface TokenRepository {

    Optional<AuthTokenEntity> retrieveByUserId(int userId);

    Optional<AuthTokenEntity> retrieveByValue(String token);

    AuthTokenEntity persistToken(AuthTokenEntity authTokenEntity);
}

