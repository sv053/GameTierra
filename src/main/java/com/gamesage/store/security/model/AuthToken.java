package com.gamesage.store.security.model;

import com.gamesage.store.domain.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

public class AuthToken extends UsernamePasswordAuthenticationToken {

    private final int userId;
    private final String value;

    public AuthToken(int userId) {
        super(null, null);
        this.value = generateToken();
        this.userId = userId;
        setAuthenticated(false);
    }

    public AuthToken(String value, int userId) {
        super(null, null);
        this.value = value;
        this.userId = userId;
        setAuthenticated(true);
    }

    public AuthToken(User user) {
        super(user.getLogin(), user.getPassword());
        this.value = generateToken();
        this.userId = user.getId();
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return getValue();
    }

    @Override
    public Object getPrincipal() {
        return getUserId();
    }

    public String getValue() {
        return value;
    }

    public int getUserId() {
        return userId;
    }

    public String generateToken() {
        return String.format("%s - %s", new Timestamp(System.currentTimeMillis()).getTime(), UUID.randomUUID());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuthToken authToken = (AuthToken) o;

        if (userId != authToken.userId) return false;
        return Objects.equals(value, authToken.value);
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AuthToken{" +
                ", token='" + value + '\'' +
                ", userId=" + userId +
                '}';
    }
}

