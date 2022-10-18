package com.gamesage.store.domain.repository;

import com.gamesage.store.security.model.VerificationToken;

import java.util.Optional;

public interface UserSecurityRepository extends UserFunctionRepository {

    Optional<VerificationToken> findToken(int userId);

    VerificationToken createToken(int userId);
}

