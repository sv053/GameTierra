package com.gamesage.store.security.auth.provider;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.security.model.AuthToken;
import com.gamesage.store.security.service.AuthService;
import com.gamesage.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class TokenAuthProvider implements AuthenticationProvider {
    @Autowired
    private final BCryptPasswordEncoder encoder;
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

    public TokenAuthProvider(AuthService authService, UserService userService, BCryptPasswordEncoder encoder) {
        this.authService = authService;
        this.userService = userService;
        this.encoder = encoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        User persistentUser = userService.findByLogin(username);

        if (encoder.matches(password, persistentUser.getPassword())) {
            SecurityContextHolder.getContext().setAuthentication(authentication);

            AuthToken authToken = new AuthToken(persistentUser);
            authToken.setDetails(userService.loadUserByUsername(username));

            return authToken;
        }
        return authentication;
    }

    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(AuthToken.class);
    }

    private Authentication checkPassword(UserDetails user,
                                         String rawPassword) {
        if (encoder.matches(rawPassword, user.getPassword())) {
            User dbUser = userService.findByLogin(user.getUsername());
            var token = new AuthToken(dbUser.getId());
            return token;
        } else {
            throw new BadCredentialsException("Bad credentials");
        }
    }
}

