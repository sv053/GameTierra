package com.gamesage.store.security.auth.filter;

import com.gamesage.store.security.auth.HeaderName;
import com.gamesage.store.security.auth.manager.AuthManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class PreAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

    private static final Logger logger = LoggerFactory.getLogger(PreAuthenticationFilter.class);

    private AuthManager authManager;

    private String getTokenValue(HttpServletRequest httpServletRequest) {
        return Collections.list(httpServletRequest.getHeaderNames()).stream()
                .filter(header -> header.equalsIgnoreCase(HeaderName.TOKEN_HEADER))
                .map(header -> httpServletRequest.getHeader(header))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        var httpResponse = (HttpServletResponse) servletResponse;
        var httpRequest = (HttpServletRequest) servletRequest;
        String requestId = getTokenValue(httpRequest);

        if (requestId == null || requestId.isBlank()) {
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.error(servletRequest.toString());
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);

    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getUserPrincipal();
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader(HeaderName.TOKEN_HEADER);
    }
}

