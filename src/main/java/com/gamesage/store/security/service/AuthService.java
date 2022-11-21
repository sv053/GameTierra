package com.gamesage.store.security.service;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.db.DbTokenRepository;
import com.gamesage.store.domain.repository.db.DbUserRepository;
import com.gamesage.store.exception.EntityNotFoundException;
import com.gamesage.store.security.auth.HeaderName;
import com.gamesage.store.security.model.AuthToken;
import com.gamesage.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final BCryptPasswordEncoder encoder;
    private final UserService userService;
    private final DbTokenRepository tokenRepository;


    @Autowired
    public AuthService(BCryptPasswordEncoder encoder, DbUserRepository userRepository,
                       UserService userService, DbTokenRepository tokenRepository) {
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

    public boolean userExists(String login, String pass) {
        String storedPass = userService.loadUserByUsername(login).getPassword();
        return encoder.matches(pass, storedPass);
    }

    private AuthToken provideWithToken(int userId) {
        Optional<AuthToken> token = findToken(userId);
        return token.orElseGet(() -> saveToken(new AuthToken(userId)));
    }

    public AuthToken provideTokenForCheckedUser(int id) {
        return provideWithToken(id);
    }

    public AuthToken saveToken(AuthToken authToken) {
        return tokenRepository.persistToken(authToken);
    }

    public ResponseEntity registerUser(User user) {
        AuthToken token = null;
        HttpHeaders responseHeaders = new HttpHeaders();
        boolean userExists = userExists(user.getLogin(), user.getPassword());
        if (userExists) {
            token = provideTokenForCheckedUser(user.getId());
            responseHeaders.set(HeaderName.TOKEN_HEADER, token.getValue());

        }
        return userExists ? ResponseEntity.ok()
                .headers(responseHeaders)
                .body(token.getValue()) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}

