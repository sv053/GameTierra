package com.gamesage.store.controller;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.security.auth.provider.AuthProvider;
import com.gamesage.store.security.service.AuthService;
import com.gamesage.store.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final AuthService authService;
    private final UserService userService;
    private final AuthProvider authProvider;
    private final BCryptPasswordEncoder encoder;

    public LoginController(AuthService authService, AuthProvider authProvider, UserService userService, BCryptPasswordEncoder encoder) {
        this.authService = authService;
        this.authProvider = authProvider;
        this.userService = userService;
        this.encoder = encoder;
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody User user,
                                   HttpServletRequest request,
                                   HttpServletResponse response,
                                   HttpSession httpSession,
                                   @RequestHeader Map<String, String> headers) {
        logger.error(response.getHeaderNames().toString());
        ResponseEntity responseEntity;
        HttpHeaders responseHeaders = new HttpHeaders();

        UsernamePasswordAuthenticationToken token = new
                UsernamePasswordAuthenticationToken(
                user.getLogin(),
                user.getPassword());

        Authentication authResult = authProvider.authenticate(token);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(authResult);
        boolean isAuth = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
        responseHeaders.set("X-Auth-Token", authResult.getCredentials().toString());

        if (isAuth) {
            responseEntity = ResponseEntity.ok()
                    .headers(responseHeaders)
                    .body(authResult.getCredentials().toString());
        } else {
            responseEntity = ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .headers(responseHeaders)
                    .body("hiuhiu");
        }
        return responseEntity;
    }
}

