package com.gamesage.store.security.service;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.security.auth.HeaderName;
import com.gamesage.store.security.model.AuthToken;
import com.gamesage.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    private final BCryptPasswordEncoder encoder;
    private final UserService userService;
    private final TokenService tokenService;

    @Autowired
    public AuthService(BCryptPasswordEncoder encoder, UserService userService, TokenService tokenService) {
        this.encoder = encoder;
        this.userService = userService;
        this.tokenService = tokenService;
    }

    public boolean userExists(String login, String pass) {
        String storedPass = userService.loadUserByUsername(login).getPassword();
        return encoder.matches(pass, storedPass);
    }

    private AuthToken provideWithToken(User user) {
        Optional<AuthToken> token = tokenService.findToken(user.getId());
        return token.orElseGet(() -> tokenService.saveToken(new AuthToken(tokenService.generateToken())));
    }

    public ResponseEntity<?> loginUser(User user) {
        AuthToken token = null;
        HttpHeaders responseHeaders = new HttpHeaders();
        boolean userExists = userExists(user.getLogin(), user.getPassword());
        if (userExists) {
            User persistedUser = userService.findByLogin(user.getLogin());
            token = provideWithToken(persistedUser);
            responseHeaders.set(HeaderName.TOKEN_HEADER, token.getValue());
        }
        return userExists ? ResponseEntity.ok()
                .headers(responseHeaders)
                .body(token.getValue()) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        return tokenService.findUserDetailsByTokenValue((String) token.getCredentials());
    }
}

