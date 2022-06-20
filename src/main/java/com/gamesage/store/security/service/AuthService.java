package com.gamesage.store.security.service;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;

    @Autowired
    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public boolean checkIfUserExists(String login, String pass) {
        User user = userService.findByLogin(login);
        return user != null && user.getLogin().equals(pass);
    }
}

