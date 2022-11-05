package com.gamesage.store.security.auth.manager;

import com.gamesage.store.security.auth.provider.TokenAuthProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class AuthManager implements AuthenticationManager {

    private final TokenAuthProvider tokenAuthProvider;
    //  private final

    public AuthManager(TokenAuthProvider tokenAuthProvider) {
        this.tokenAuthProvider = tokenAuthProvider;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // if()
        return tokenAuthProvider.authenticate(authentication);
    }
}

