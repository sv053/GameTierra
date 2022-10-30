package com.gamesage.store.security.filter;

import com.gamesage.store.security.model.AuthenticationToken;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class TokenPreAuthenticatedProcessingFilter extends AbstractPreAuthenticatedProcessingFilter {

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        String xAuthToken = request.getHeader("X-Auth-Token");

        if (!StringUtils.hasText(xAuthToken)) {
            return null;
        }

        return new AuthenticationToken(xAuthToken);
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return request.getHeader("X-Auth-Token");
    }
}
