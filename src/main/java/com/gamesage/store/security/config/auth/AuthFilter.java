package com.gamesage.store.security.config.auth;

import com.gamesage.store.security.model.AuthToken;
import com.gamesage.store.security.service.AuthService;
import org.h2.util.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class AuthFilter extends UsernamePasswordAuthenticationFilter {
    public static final String TOKEN_HEADER = "x-auth-token";
    private final AuthProvider authProvider;
    private final AuthService authService;

    public AuthFilter(AuthProvider authProvider, AuthService authService) {
        super(null);
        this.authProvider = authProvider;
        this.authService = authService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        var r = request;
        final String req = getTokenValue((HttpServletRequest) request);
        final String token = ((HttpServletRequest) request).getHeader(TOKEN_HEADER);
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
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException {
        final String tokenValue = getTokenValue(httpServletRequest);

        if (StringUtils.isNullOrEmpty(tokenValue)) {
            return null;
        }

        AuthToken token = authService.provideTokenForCheckedUser(SecurityContextHolder.getContext().getAuthentication().getName());// httpServletResponse.getHeader("x-auth-token"));
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

