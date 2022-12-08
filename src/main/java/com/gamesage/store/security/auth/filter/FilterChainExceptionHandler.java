package com.gamesage.store.security.auth.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class FilterChainExceptionHandler extends OncePerRequestFilter {

    private final HandlerExceptionResolver resolver;

    public FilterChainExceptionHandler(HandlerExceptionResolver handlerExceptionResolver) {
        this.resolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            super.logger.error("Spring Security Filter Chain Exception:", e);
            resolver.resolveException(request, response, null, e);
        }
    }
}

