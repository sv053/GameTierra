package com.gamesage.store.service;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.TokenRepository;
import com.gamesage.store.domain.repository.model.AuthTokenEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;
    private final UserService userService;

    public TokenService(TokenRepository tokenRepository, UserService userService) {
        this.tokenRepository = tokenRepository;
        this.userService = userService;
    }

    public UserDetails findUserDetailsByTokenValue(String tokenValue) {
        AuthTokenEntity entity = tokenRepository.retrieveByValue(tokenValue).orElseThrow(() -> new IllegalArgumentException("Token not found " + tokenValue));
        User user = userService.findById(entity.getUserId());
        return userService.loadUserByUsername(user.getLogin());
    }

    public AuthTokenEntity generateToken(User user) {
        AuthTokenEntity entity = new AuthTokenEntity(user.getId(), UUID.randomUUID().toString());
        return tokenRepository.persistToken(entity);
    }
}
