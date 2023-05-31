package com.gamesage.store.security.service;

import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.service.TokenService;
import com.gamesage.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class AuthService {

    private final UserService userService;
    private final TokenService tokenService;

    @Value("${com.gamesage.store.tokenExpiryInterval}")
    private int tokenExpiryInterval;

    @Autowired
    public AuthService(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    private Integer findUserId(User user) {
        return userService.findByCredentials(user.getLogin(), user.getPassword()).getId();
    }

    public AuthToken authenticateUser(User user) {
        LocalDateTime localDateTime = LocalDateTime.now().plus(tokenExpiryInterval, ChronoUnit.DAYS);
        return provideWithToken(findUserId(user), localDateTime);
    }

    public void revokeAccess(String token) {
        tokenService.invalidateToken(token);
    }

    private AuthToken provideWithToken(Integer id, LocalDateTime localDateTime) {
        return tokenService.createToken(
                new AuthToken(generateToken(), id, localDateTime));
    }

    private String generateToken() {
        return String.format("%s-%s", System.currentTimeMillis(), UUID.randomUUID());
    }
}

