package com.gamesage.store.service;

import com.gamesage.store.domain.model.User;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class AuthService {

    private final UserService userService;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    private boolean checkIfCredentialsAreTheSame(String login1, String login2, String pass1, String pass2) {
        return login1.equals(login2) && pass1.equals(pass2);
    }

    public boolean checkIfUserExists(String login, String pass) {
        User user = findUserByLogin(login);
        return checkIfCredentialsAreTheSame(login, user.getLogin(), pass, user.getPassword());
    }

    private User findUserByLogin(String login) {
        return userService.findAll()
                .stream()
                .filter(u -> u.getLogin().equals(login))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }
}

