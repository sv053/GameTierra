package com.gamesage.store.service;

import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.domain.repository.TokenRepository;
import com.gamesage.store.exception.WrongCredentialsException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public Optional<AuthToken> findTokenById(Integer userId) {
        return tokenRepository.findById(userId);
    }

    public AuthToken findToken(String token) {
        return tokenRepository.findByValue(token).orElseThrow(WrongCredentialsException::new);
    }

    public AuthToken createToken(AuthToken authToken) {
        return tokenRepository.createOne(authToken);
    }
}

