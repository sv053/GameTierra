package com.gamesage.store.service;

import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.domain.repository.TokenRepository;
import com.gamesage.store.exception.WrongCredentialsException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;
    private final BCryptPasswordEncoder encoder;

    public TokenService(TokenRepository tokenRepository, BCryptPasswordEncoder encoder) {
        this.tokenRepository = tokenRepository;
        this.encoder = encoder;
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
        AuthToken encryptedToken = new AuthToken(
                encoder.encode(authToken.getValue()),
                authToken.getUserId(),
                authToken.getExpirationDateTime());

        return tokenRepository.createOne(encryptedToken);
    }

    @Scheduled(cron = "${com.gamesage.store.cleanup}")
    public void removeExpiredTokens() {
        tokenRepository.removeExpired();
    }

    public void invalidateToken(String token) {
        tokenRepository.removeByValue(token);
    }
}

