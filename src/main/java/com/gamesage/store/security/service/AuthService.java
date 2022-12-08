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
        UserDetails foundUser = findUserByCredentials(user.getLogin(), user.getPassword());
        return provideWithToken(foundUser.getUsername());
    }

    private AuthToken provideWithToken(String login) {
        return tokenService.findTokenByLogin(login)
                .orElseGet(() -> tokenService.createToken(new AuthToken(generateToken(), login)));
    }

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        return findUserDetailsByTokenValue((String) token.getCredentials());
    }

    public UserDetails findUserDetailsByTokenValue(String tokenValue) {
        AuthToken entity = tokenService.findToken(tokenValue);
        return userService.loadUserByUsername(entity.getUserLogin());
    }

    private UserDetails findUserByCredentials(String login, String pass) {
        UserDetails user = userService.loadUserByUsername(login);
        if (!encoder.matches(pass, user.getPassword()))
            throw new WrongCredentialsException();
        return user;
    }

    private String generateToken() {
        return String.format("%s-%s", System.currentTimeMillis(), UUID.randomUUID());
    }
}

