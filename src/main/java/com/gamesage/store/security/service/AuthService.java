package com.gamesage.store.security.service;

import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.exception.WrongCredentialsException;
import com.gamesage.store.service.TokenService;
import com.gamesage.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;

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

    public AuthToken authenticateUser(User user) {
        try {
            UserDetails foundUser = findUserByCredentials(user.getLogin(), user.getPassword());
            return provideWithToken(foundUser.getUsername());
        } catch (Exception e) {
            throw new WrongCredentialsException();
        }
    }

    private AuthToken provideWithToken(String login) {
        return tokenService.findTokenByLogin(login)
                .orElseGet(() -> tokenService.saveToken(new AuthToken(generateToken(), login)));
    }

    public UserDetails findUserDetailsByTokenValue(String tokenValue) {
        AuthToken entity = tokenService.findToken(tokenValue);
        return userService.loadUserByUsername(entity.getUserLogin());
    }

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        return findUserDetailsByTokenValue((String) token.getCredentials());
    }

    private UserDetails findUserByCredentials(String login, String pass) {
        UserDetails storedUser = userService.loadUserByUsername(login);
        try {
            encoder.matches(pass, storedUser.getPassword());
            return storedUser;
        } catch (NullPointerException e) {
            throw new WrongCredentialsException();
        }
    }

    private String generateToken() {
        return String.format("%s-%s", new Timestamp(System.currentTimeMillis()).getTime(), UUID.randomUUID());
    }
}

