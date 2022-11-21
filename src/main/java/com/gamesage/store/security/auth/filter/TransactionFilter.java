package com.gamesage.store.security.auth.filter;

import com.gamesage.store.security.auth.HeaderName;
import com.gamesage.store.security.auth.manager.AuthManager;
import com.gamesage.store.security.model.AuthToken;
import com.gamesage.store.security.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TransactionFilter extends AbstractAuthenticationProcessingFilter {

    private static final Logger logger = LoggerFactory.getLogger(TransactionFilter.class);
    private final AuthManager authManager;
    private final AuthService authService;

    public TransactionFilter(String defaultUrl, AuthManager authManager, AuthService authService) {
        super(defaultUrl);
        super.setAuthenticationManager(authManager);
        this.authManager = authManager;
        this.authService = authService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (((HttpServletRequest) request).getHeader(HeaderName.TOKEN_HEADER) != null) {

            attemptAuthentication(httpRequest, httpResponse);
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setHeader(
                    "Baeldung-Example-Filter-Header", "Value-Filter");
        }
        chain.doFilter(request, response);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        AuthToken token = authService.findToken(httpServletRequest.getHeader(HeaderName.TOKEN_HEADER));
        Authentication responseAuthentication = authManager.authenticate(token);
        if (responseAuthentication == null || !responseAuthentication.isAuthenticated()) {
            throw new InternalAuthenticationServiceException("Unable to authenticate Domain User for provided credentials");
        }
        logger.debug("User successfully authenticated");
        return responseAuthentication;
    }
}

