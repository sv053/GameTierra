package com.gamesage.store.security.service;

import com.gamesage.store.domain.repository.TokenRepository;
import com.gamesage.store.exception.EntityNotFoundException;
import com.gamesage.store.security.model.AuthToken;
import com.gamesage.store.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;
    private final UserService userService;

    public TokenService(TokenRepository tokenRepository, UserService userService) {
        this.tokenRepository = tokenRepository;
        this.userService = userService;
    }

    public Optional<AuthToken> findTokenByLogin(String userLogin) {
        return tokenRepository.findByUserLogin(userLogin);
    }

    public AuthToken findToken(String token) {
        return tokenRepository.findByValue(token).orElseThrow(() -> new EntityNotFoundException(String.valueOf(token)));
    }

    public AuthToken saveToken(AuthToken AuthToken) {
        return tokenRepository.saveToken(AuthToken);
    }

    public UserDetails findUserDetailsByTokenValue(String tokenValue) {
        AuthToken entity = findToken(tokenValue);
        return userService.loadUserByUsername(entity.getUserLogin());
    }

    public String generateToken() {
        return String.format("%s-%s", new Timestamp(System.currentTimeMillis()).getTime(), UUID.randomUUID());
    }
}

