package com.gamesage.store.security.service;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.db.DbTokenRepository;
import com.gamesage.store.domain.repository.db.DbUserRepository;
import com.gamesage.store.exception.EntityNotFoundException;
import com.gamesage.store.security.model.AuthToken;
import com.gamesage.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder encoder;
    private final UserService userService;
    private final DbTokenRepository tokenRepository;


    @Autowired
    public AuthService(UserDetailsService userDetailsService, BCryptPasswordEncoder encoder, DbUserRepository userRepository,
                       UserService userService, DbTokenRepository tokenRepository) {
        this.userDetailsService = userDetailsService;
        this.encoder = encoder;
        this.userService = userService;
        this.tokenRepository = tokenRepository;
    }

    public Optional<AuthToken> findToken(int userId) {
        return tokenRepository.retrieveByUserId(userId);
    }

    public AuthToken findToken(String token) {
        return tokenRepository.retrieveByValue(token).orElseThrow(() -> new EntityNotFoundException(String.valueOf(token)));
    }

    public boolean checkIfUserExists(String login, String pass) {
        String storedPass = userDetailsService.loadUserByUsername(login).getPassword();
        return encoder.matches(pass, storedPass);
    }

    private AuthToken provideWithToken(int userId) {
        Optional<AuthToken> token = findToken(userId);
        AuthToken createdToken = new AuthToken(userId);
        if (!token.isPresent()) {
            createdToken = saveToken(userId);
        }
        return createdToken;
    }

    public User provideCheckedUserWithToken(String login) {
        User user = userService.findByLogin(login);
        provideWithToken(user.getId()).getValue();
        return user;
    }

    public AuthToken saveToken(int userId) {
        return tokenRepository.persistToken(userId);
    }
}

