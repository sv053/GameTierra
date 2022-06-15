package com.gamesage.store.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Value("${spring.security.user.name}")
    private String name;
    @Value("${spring.security.user.password}")
    private String password;

    public boolean ifCredentialsExist(String login, String pass) {
        return login.equals(name) && password.equals(pass);
    }
}

