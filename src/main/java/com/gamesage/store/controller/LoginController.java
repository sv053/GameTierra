package com.gamesage.store.controller;

import com.gamesage.store.service.AuthService;
import com.gamesage.store.service.UserService;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/{user}")
    public ResponseEntity login(@PathVariable String user, @RequestBody String password) throws Exception {
        User eligibleUser = authService.getUser();
        if (UserService.matchLoginPassword(user, eligibleUser.getName(), password, eligibleUser.getPassword()))
            return ResponseEntity.ok("U r in!");
        return ResponseEntity.notFound().build();
    }
}

