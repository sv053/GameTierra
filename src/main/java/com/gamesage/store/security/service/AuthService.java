package com.gamesage.store.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public AuthService(UserDetailsService userDetailsService, BCryptPasswordEncoder encoder) {
        this.userDetailsService = userDetailsService;
        this.encoder = encoder;
    }

    public boolean checkIfUserExists(String login, String pass) {
        String storedPass = userDetailsService.loadUserByUsername(login).getPassword();
        return encoder.matches(pass, storedPass);
    }
}

