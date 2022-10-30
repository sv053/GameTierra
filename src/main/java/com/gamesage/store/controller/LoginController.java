package com.gamesage.store.controller;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.model.AuthTokenEntity;
import com.gamesage.store.security.service.AuthService;
import com.gamesage.store.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final AuthService authService;
    public static final String TOKEN_HEADER = "X-Auth-Token";
    private final TokenService tokenService;


    public LoginController(AuthService authService, TokenService tokenService) {
        this.authService = authService;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<User> login(@RequestBody User user) {
        User authenticated = authService.findUser(user.getLogin(), user.getPassword());
        AuthTokenEntity tokenEntity = tokenService.generateToken(authenticated);
        return ResponseEntity
                .ok()
                .header(TOKEN_HEADER, tokenEntity.getValue())
                .body(authenticated);
    }
}

