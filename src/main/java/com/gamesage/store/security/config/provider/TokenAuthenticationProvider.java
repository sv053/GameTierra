package com.gamesage.store.security.config.provider;

import com.gamesage.store.domain.model.User;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Optional;

public class TokenAuthenticationProvider implements AuthenticationProvider {
    private final TokenService tokenService;

    public TokenAuthenticationProvider(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Optional token = (Optional) authentication.getPrincipal();
        User user = (User) authentication.getPrincipal();
        if (!token.isPresent() || token.get()) {
            throw new BadCredentialsException("Invalid token");
        }
        if (!tokenService.contains(user.getId())) {
            throw new BadCredentialsException("Invalid token or token expired");
        }
        return tokenService.retrieve(user.getId());
    }

    @Override
    public boolean supports(Class<> authentication) {
        return authentication.equals(PreAuthenticatedAuthenticationToken.class);
    }
}
