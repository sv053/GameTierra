package com.gamesage.store.security.service;

import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.service.TokenService;
import com.gamesage.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    private final UserService userService;
    private final TokenService tokenService;

    @Autowired
    public AuthService(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    public AuthToken authenticateUser(User user) {
        User foundUser = userService.findByCredentials(user.getLogin(), user.getPassword());
        return provideWithToken(foundUser.getId());
    }

    private AuthToken provideWithToken(Integer id) {
        return tokenService.findTokenById(id)
                .orElseGet(() -> tokenService.createToken(new AuthToken(generateToken(), id)));
    }

    private String generateToken() {
        return String.format("%s-%s", System.currentTimeMillis(), UUID.randomUUID());
    }
}

