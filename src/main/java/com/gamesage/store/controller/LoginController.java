package com.gamesage.store.controller;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.security.config.auth.AuthProvider;
import com.gamesage.store.security.model.AuthToken;
import com.gamesage.store.security.service.AuthService;
import com.gamesage.store.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final AuthService authService;
    public static final String TOKEN_HEADER = "x-auth-token";
    private final UserService userService;
    private final AuthProvider authProvider;


    public LoginController(AuthService authService, AuthProvider authProvider, UserService userService) {
        this.authService = authService;
        this.authProvider = authProvider;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> login(@RequestBody User user, HttpServletRequest request) {
        AuthToken authToken = new AuthToken(user);
        Authentication auth = authProvider.authenticate(authToken);

        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        HttpSession session = request.getSession(true);
        session.setAttribute(TOKEN_HEADER, sc);

        return ResponseEntity
                .ok()
                .header(TOKEN_HEADER, authToken.getValue())
                .body(user);
    }
}

