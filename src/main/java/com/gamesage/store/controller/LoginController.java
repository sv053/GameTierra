package com.gamesage.store.controller;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.security.auth.HeaderName;
import com.gamesage.store.security.service.AuthService;
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
        return ResponseEntity.ok()
                .headers(httpHeaders ->
                        httpHeaders.set(HeaderName.TOKEN_HEADER, authService.loginUser(user)))
                .body(user);
    }
}

