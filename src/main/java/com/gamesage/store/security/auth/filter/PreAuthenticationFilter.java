package com.gamesage.store.security.auth.filter;

import com.gamesage.store.exception.WrongCredentialsException;
import com.gamesage.store.security.auth.HeaderName;
import com.gamesage.store.security.model.SSAuthToken;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;

public class PreAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(HeaderName.TOKEN_HEADER);
        return token == null ? new WrongCredentialsException() : new SSAuthToken(token);
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader(HeaderName.TOKEN_HEADER);
    }
}

