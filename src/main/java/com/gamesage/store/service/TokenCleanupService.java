package com.gamesage.store.service;

import com.gamesage.store.domain.repository.TokenRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TokenCleanupService {

    private final TokenRepository tokenRepository;

    public TokenCleanupService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Scheduled(cron = "${com.gamesage.store.cleanup}")
    public void removeExpiredTokens() {
        tokenRepository.removeExpired();
    }
}

