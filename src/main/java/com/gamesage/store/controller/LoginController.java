package com.gamesage.store.controller;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.security.auth.HeaderName;
import com.gamesage.store.security.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.Base64;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    private static final String getBasicAuthenticationHeader(String username, String password) {
        String valueToEncode = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
    }

    @PostMapping("/yeah")
//    public ResponseEntity<?> login(@RequestBody User user, HttpServletRequest request) {
    public void login(@RequestBody User user,
                      HttpServletRequest servletRequest,
                      HttpServletResponse servletResponse) {

        var authResult = authService.registerUser(user);
        if (authResult.getHeaders().containsKey(HeaderName.TOKEN_HEADER)) {
            try {
                HttpRequest.newBuilder()
                        .GET()
                        .uri(new URI(servletRequest.getScheme() + "/users"))
                        .header("Authorization", getBasicAuthenticationHeader(user.getLogin(), user.getPassword()))
                        .build();
                String token = authResult.getHeaders().get(HeaderName.TOKEN_HEADER).get(0);
                Cookie cookie = new Cookie(HeaderName.TOKEN_HEADER, token);
                servletResponse.addCookie(cookie);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @PostMapping("/auth")
    public ResponseEntity<?> checkAuth(@RequestBody User user) {
        return authService.registerUser(user);
    }
}

