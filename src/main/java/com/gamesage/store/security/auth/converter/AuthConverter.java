package com.gamesage.store.security.auth.converter;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;

import javax.servlet.http.HttpServletRequest;

public class AuthConverter implements AuthenticationConverter {
    @Override
    public Authentication convert(HttpServletRequest httpServletRequest) {
        return null;
    }
}
