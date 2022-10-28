package com.gamesage.store.security.config.auth;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.security.model.AppUser;
import com.gamesage.store.security.model.AuthToken;
import com.gamesage.store.security.service.AuthService;
import com.gamesage.store.service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthProvider implements AuthenticationProvider {
    private final AuthService authService;
    private final UserService userService;
    private final BCryptPasswordEncoder encoder;

    public AuthProvider(AuthService authService, UserService userService, BCryptPasswordEncoder encoder) {
        this.authService = authService;
        this.userService = userService;
        this.encoder = encoder;
    }

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        String login = auth.getPrincipal().toString();
        String password = String.valueOf(auth.getCredentials());

        User user = userService.findByLogin(login);//(login);
        AuthToken authToken = authService.provideTokenForCheckedUser(login);

        UserDetails userDetails = new AppUser(user);

        if (!encoder.matches(password, user.getPassword())) {
            return null;
        }

        authToken.setAuthenticated(true);
        authToken.setDetails(userDetails);
        authService.saveToken(authToken);
        return authToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AuthToken.class.isAssignableFrom(authentication);
    }
}

