package com.gamesage.store.service;

import com.gamesage.store.domain.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;
    @Value("${spring.security.user.name}")
    private String name;
    @Value("${spring.security.user.password}")
    private String password;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public boolean checkIfCredentialsExist(String login, String pass) {
        return login.equals(name) && password.equals(pass);
    }

    private boolean checkIfCredentialsAreTheSame(String login1, String login2, String pass1, String pass2) {
        return login1.equals(login2) && pass1.equals(pass2);
    }

    public boolean checkIfUserExists(String login, String pass) {
        User user = findUserByLogin(login);
        return checkIfCredentialsAreTheSame(login, user.getLogin(), pass, user.getPassword());
    }

    private User findUserByLogin(String login) {
        return userService.findByLogin(login);
    }
}

