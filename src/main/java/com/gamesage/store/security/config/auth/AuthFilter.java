package com.gamesage.store.security.config.auth;

import com.gamesage.store.security.model.AuthToken;
import org.h2.util.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class AuthFilter extends AbstractAuthenticationProcessingFilter {
    public static final String TOKEN_HEADER = "x-auth-token";

    public AuthFilter(RequestMatcher requestMatcher) {
        super(requestMatcher);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        final String token = getTokenValue((HttpServletRequest) request);

        if (StringUtils.isNullOrEmpty(token)) {
            chain.doFilter(request, response);
            return;
        }
        this.setAuthenticationSuccessHandler((request1, response1, authentication) -> {
            chain.doFilter(request1, response1);
        });

        super.doFilter(request, response, chain);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        final String tokenValue = getTokenValue(httpServletRequest);

        if (StringUtils.isNullOrEmpty(tokenValue)) {
            return null;
        }

        AuthToken token = new AuthToken(Integer.parseInt(httpServletResponse.getHeader("")));
        token.setDetails(authenticationDetailsSource.buildDetails(httpServletRequest));

        return this.getAuthenticationManager().authenticate(token);
    }

    private String getTokenValue(HttpServletRequest httpServletRequest) {
        return Collections.list(httpServletRequest.getHeaderNames()).stream()
                .filter(header -> header.equalsIgnoreCase(TOKEN_HEADER))
                .map(header -> httpServletRequest.getHeader(header))
                .findFirst()
                .orElse(null);
    }
}

