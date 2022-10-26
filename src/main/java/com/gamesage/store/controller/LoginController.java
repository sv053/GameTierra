package com.gamesage.store.controller;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.security.config.auth.AuthProvider;
import com.gamesage.store.security.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final AuthService authService;
    private final AuthProvider ap;

    public LoginController(AuthService authService, AuthProvider ap) {
        this.authService = authService;
        this.ap = ap;
    }

    @PostMapping("/how")
    public ResponseEntity<User> login(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {
        User existingUser;
        if (authService.checkIfUserExists(user.getLogin(), user.getPassword())) {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            logger.error("currentUser " + authentication.getPrincipal().toString());

            return ResponseEntity
                    .ok()
                    .header("x-auth-token", authentication.getCredentials().toString())
                    .body((User) authentication.getPrincipal());
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/")
    public Authentication login1(@RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication;
    }

//    @PostMapping("/")
//    public AuthToken login(@RequestBody User user) {
//        AuthToken authToken;
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authService.checkIfUserExists(user.getLogin(), user.getPassword())) {
//
//            existingUser = authService.provideTokenForCheckedUser(user.getLogin());
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            logger.error("currentUser " + authentication.getPrincipal().toString());
//
//            return new ResponseEntity<>(authToken, HttpStatus.OK);
//        }
//        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//    }

    @GetMapping
    public String showAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ap.authenticate(authentication).getCredentials().toString();
    }
}

