package com.gamesage.store.security.auth.filter;

import com.gamesage.store.security.auth.HeaderName;
import com.gamesage.store.security.auth.provider.AuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Configuration
public class RequestValidationFilter implements Filter {
    @Autowired
    private AuthProvider authProvider;

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
            filterChain.doFilter(servletRequest, servletResponse);
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
    }
}

