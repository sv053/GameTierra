package com.gamesage.store.controller;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.security.service.AuthService;
import com.gamesage.store.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final AuthService authService;
    private final UserService userService;

    public LoginController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> login(@RequestBody User user) {
        User existingUser;
        if (authService.checkIfUserExists(user.getLogin(), user.getPassword())) {

            existingUser = authService.provideCheckedUserWithToken(user.getLogin());
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            logger.error("currentUser " + authentication.getPrincipal().toString());

            return new ResponseEntity<>(existingUser, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

}

