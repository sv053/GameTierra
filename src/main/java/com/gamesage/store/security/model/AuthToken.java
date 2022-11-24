package com.gamesage.store.security.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gamesage.store.domain.model.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class AuthToken extends AbstractAuthenticationToken {

    private final User user;
    @JsonProperty
    private final String value;

    public AuthToken(String tokenValue) {
        this(null, tokenValue, Collections.emptyList());
    }

    public AuthToken(User user, String tokenValue, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.value = tokenValue;
        this.user = user;
    }

    @Override
    public Object getCredentials() {
        return getValue();
    }

    @Override
    public Object getPrincipal() {
        return getUser();
    }

    public String getValue() {
        return value;
    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AuthToken thatToken = (AuthToken) o;
        return Objects.equals(user, thatToken.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), user);
    }

    @Override
    public String toString() {
        return "AuthToken{" +
                ", token='" + value + '\'' +
                ", user=" + user.toString() +
                '}';
    }
}

