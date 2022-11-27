package com.gamesage.store.security.model;

import com.gamesage.store.domain.model.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class SSAuthToken extends AbstractAuthenticationToken {

    private final User user;
    private final String token;

    public SSAuthToken(String token) {
        this(null, token, Collections.emptyList());
    }

    public SSAuthToken(User user, String token, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.user = user;
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return "";
    }

    @Override
    public Object getPrincipal() {
        return getUser();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SSAuthToken that = (SSAuthToken) o;
        return Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), user);
    }
}

