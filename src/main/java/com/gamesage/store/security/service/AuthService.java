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

    public AuthToken findToken(int userId) {
        return tokenRepository.findToken(userId).orElseThrow(() -> new EntityNotFoundException(String.valueOf(userId)));
    }

    public AuthToken findToken(String token) {
        return tokenRepository.findToken(token).orElseThrow(() -> new EntityNotFoundException(String.valueOf(token)));
    }

    public boolean checkIfUserExists(String login, String pass) {
        String storedPass = userDetailsService.loadUserByUsername(login).getPassword();
        return encoder.matches(pass, storedPass);
    }

    private AuthToken provideWithToken(int userId) {
        AuthToken token = findToken(userId);
        if (null == token) {
            token = updateToken(userId);
        }
        return token;
    }

    public User provideCheckedUserWithToken(String login) {
        User user = userService.findByLogin(login);
        updateToken(user.getId());
        provideWithToken(user.getId()).getToken();
        return user;
    }

    public AuthToken updateToken(int userId) {
        return tokenRepository.createToken(userId);
    }
}

