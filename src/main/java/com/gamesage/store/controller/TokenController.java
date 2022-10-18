package com.gamesage.store.controller;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.security.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class TokenController {
    @Autowired
    private final AuthService authService;

    public TokenController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/token")
    public User getToken(@RequestParam("username") final String username) {
        return authService.provideCheckedUserWithToken(username);
    }


}
