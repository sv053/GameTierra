package com.gamesage.store.security.auth.filter;

import com.gamesage.store.security.auth.HeaderName;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;

public class PreAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader(HeaderName.TOKEN_HEADER);
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader(HeaderName.TOKEN_HEADER);
    }
}

