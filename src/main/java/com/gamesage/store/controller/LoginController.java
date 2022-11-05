package com.gamesage.store.controller;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.security.service.AuthService;
import com.gamesage.store.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final AuthService authService;
    private final BCryptPasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final UserService userService;

    public LoginController(AuthService authService, BCryptPasswordEncoder encoder, AuthenticationManager authManager, UserService userService) {
        this.authService = authService;
        this.encoder = encoder;
        this.authManager = authManager;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody User user, HttpServletRequest request) {
        return authService.registerUser(user);
    }

    @GetMapping("/auth")
    public String checkAuth() {
        return "auth string";
    }
}

