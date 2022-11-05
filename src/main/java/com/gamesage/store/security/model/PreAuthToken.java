package com.gamesage.store.security.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Collection;

public class PreAuthToken extends PreAuthenticatedAuthenticationToken {
    public PreAuthToken(Object aPrincipal, Object aCredentials) {
        super(aPrincipal, aCredentials);
    }

    public PreAuthToken(Object aPrincipal, Object aCredentials, Collection<? extends GrantedAuthority> anAuthorities) {
        super(aPrincipal, aCredentials, anAuthorities);
    }

    public String getToken() {
        return (String) getDetails();
    }

    public void setToken(String token) {
        setDetails(token);
    }
}
