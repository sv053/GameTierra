package com.gamesage.store.security.config.auth;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.security.model.AuthToken;
import com.gamesage.store.security.service.AuthService;
import com.gamesage.store.service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class AuthProvider implements AuthenticationProvider {
    private final AuthService authService;
    private final UserService userService;

    public AuthProvider(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        final AuthToken tokenContainer = (AuthToken) auth;
        final String token = tokenContainer.getValue();
        int userId = 0, tokenId = 0;

        if (null == authService.findToken(token)) {
            throw new BadCredentialsException("Invalid token - " + token);
        }

        final User user = userService.findById(userId);

        return new AuthToken(token, user.getId());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AuthToken.class.isAssignableFrom(authentication);
    }
}

