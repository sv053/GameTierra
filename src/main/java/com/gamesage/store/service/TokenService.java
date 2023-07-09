package com.gamesage.store.service;

import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.domain.repository.TokenRepository;
import com.gamesage.store.exception.WrongCredentialsException;
import com.gamesage.store.util.Parser;
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
        AuthToken tokenToSave = new AuthToken(encoder.encode(authToken.getValue()), authToken.getUserId(), authToken.getExpirationDateTime());
        AuthToken savedToken = tokenRepository.createOne(tokenToSave);
        int userId = savedToken.getUserId();
        String tokenValue = userId + Parser.DELIMITER + authToken.getValue();
        return new AuthToken(savedToken.getId(), tokenValue, userId, savedToken.getExpirationDateTime());
    }

    public AuthToken updateToken(AuthToken authToken) {
        return tokenRepository.updateByUserId(authToken);
    }

    @Scheduled(cron = "${com.gamesage.store.cleanup}")
    public void removeExpiredTokens() {
        tokenRepository.removeExpired();
    }

    public boolean invalidateToken(AuthToken authToken) {
        return tokenRepository.removeByUserId(authToken.getUserId(), authToken.getValue());
    }
}

