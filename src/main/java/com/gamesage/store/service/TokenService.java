package com.gamesage.store.service;

import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.domain.repository.TokenRepository;
import com.gamesage.store.exception.WrongCredentialsException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public Optional<AuthToken> findTokenById(Integer id) {
        return tokenRepository.findById(id);
    }

    public Optional<AuthToken> findTokenByUserId(Integer userId) {
        return tokenRepository.findByUserId(userId);
    }

    public AuthToken findToken(String token) {
        return tokenRepository.findByValue(token).orElseThrow(WrongCredentialsException::new);
    }

    public AuthToken createToken(AuthToken authToken) {
        return tokenRepository.createOne(authToken);
    }

    public AuthToken updateToken(AuthToken authToken) {
        return tokenRepository.updateByUserId(authToken);
    }

    @Scheduled(cron = "${com.gamesage.store.cleanup}")
    public void removeExpiredTokens() {
        tokenRepository.removeExpired();
    }

    public void invalidateToken(Integer id) {
        tokenRepository.removeByUserId(id);
    }
}

