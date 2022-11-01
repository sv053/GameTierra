package com.gamesage.store.security.auth.provider;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.security.auth.HeaderName;
import com.gamesage.store.security.auth.manager.AuthManager;
import com.gamesage.store.security.model.AuthToken;
import com.gamesage.store.security.service.AuthService;
import com.gamesage.store.service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuthProvider implements AuthenticationProvider {
    private final AuthService authService;
    private final UserService userService;
    private final BCryptPasswordEncoder encoder;

    public AuthProvider(AuthService authService, UserService userService, BCryptPasswordEncoder encoder, AuthManager authManager) {
        this.authService = authService;
        this.userService = userService;
        this.encoder = encoder;
    }

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        final HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String userLogin = auth.getName();
        String password = auth.getCredentials().toString();
        if (null == request.getHeader(HeaderName.TOKEN_HEADER)) {
            UserDetails persistentUser = userService.loadUserByUsername(userLogin);
            return checkPassword(persistentUser, password);
        }
        throw new BadCredentialsException("Bad credentials");
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

