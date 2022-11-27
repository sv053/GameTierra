package com.gamesage.store.controller;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.security.auth.HeaderName;
import com.gamesage.store.security.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<User> login(@RequestBody User user) {
        HttpHeaders responseHeaders = new HttpHeaders();
        String token = authService.loginUser(user);
        responseHeaders.set(HeaderName.TOKEN_HEADER, token);

        return token.isEmpty() ? new ResponseEntity<>(user, HttpStatus.NOT_FOUND) :
                ResponseEntity.ok()
                        .headers(responseHeaders)
                        .body(user);
    }
}

