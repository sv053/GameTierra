package com.gamesage.store.controller;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.security.auth.HeaderName;
import com.gamesage.store.security.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/logout")
public class LogoutController {

    private final AuthService authService;

    public LogoutController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<User> logout(@RequestBody User user, HttpServletRequest request) {
        authService.revokeAccess(user, request.getHeader(HeaderName.TOKEN_HEADER));
        return ResponseEntity.ok()
                .header(HeaderName.TOKEN_HEADER, "")
                .body(user);
    }
}

