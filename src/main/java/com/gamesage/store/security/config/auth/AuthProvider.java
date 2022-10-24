package com.gamesage.store.security.config.auth;

import com.gamesage.store.security.model.AuthToken;
import com.gamesage.store.security.service.AuthService;
import com.gamesage.store.service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class AuthProvider implements AuthenticationProvider {
    private final AuthService authService;
    private final UserService userService;

    public AuthProvider(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        String login = auth.getName();
        String token = String.valueOf(auth.getCredentials());

//        final String tokenContainer = auth.getCredentials();
//        final String token = tokenContainer.getValue();
//
//        if (null == userService.findToken(token)) {
//            throw new BadCredentialsException("Invalid token - " + token);
//        }

        //  final User existingUser = userService.findByLogin(login);

        return authService.provideTokenForCheckedUser(login);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AuthToken.class.isAssignableFrom(authentication);
    }
}

