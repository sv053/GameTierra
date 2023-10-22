package com.gamesage.store.service;

import com.gamesage.store.domain.repository.TokenRepository;
import com.gamesage.store.domain.repository.db.DbTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TokenCleanupService {

    private static final Logger logger = LoggerFactory.getLogger(DbTokenRepository.class);
    private final TokenRepository tokenRepository;

    public TokenCleanupService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Scheduled(cron = "${com.gamesage.store.cleanup}")
    public void removeExpiredTokens() {
        int removedTokensAmount = tokenRepository.removeExpired();
        logger.info("Removed {} tokens", removedTokensAmount);
    }
}

