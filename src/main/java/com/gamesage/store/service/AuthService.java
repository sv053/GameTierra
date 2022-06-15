package com.gamesage.store.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Service
@ConfigurationProperties(prefix = "spring.security.user")
@Configuration
public class AuthService {

    private String name;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean findByLoginPassword(String login, String pass) {
        return login.equals(name) && password.equals(pass);
    }
}

