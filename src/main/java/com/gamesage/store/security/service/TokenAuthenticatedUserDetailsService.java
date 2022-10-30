package com.gamesage.store.security.service;

import com.gamesage.store.security.model.AuthenticationToken;
import com.gamesage.store.service.TokenService;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class TokenAuthenticatedUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    private final TokenService tokenService;

    public TokenAuthenticatedUserDetailsService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        AuthenticationToken principal = (AuthenticationToken) token.getPrincipal();
        String xAuthToken = principal.getXAuthToken();
        return tokenService.findUserDetailsByTokenValue(xAuthToken);
    }
}
