package com.gamesage.store.service;

import com.gamesage.store.configs.PropertiesConverter;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    private final User user;
    private final PropertiesConverter properties;

    public AuthService(PropertiesConverter properties) {
        this.properties = properties;
        this.user = new User();
        user.setName(properties.getName());
        user.setPassword(properties.getPassword());
        user.setRoles(List.of("ADMIN"));
    }

    public User getUser() {
        return user;
    }
}

