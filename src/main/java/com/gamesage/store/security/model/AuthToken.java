package com.gamesage.store.security.model;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.sql.Timestamp;
import java.util.UUID;

public class AuthToken extends AbstractAuthenticationToken {

    private final int userId;
    private long id;
    private String token;

    public AuthToken(int userId) {
        super(null);
        this.token = generateToken();
        this.userId = userId;
        setAuthenticated(false);
    }

    public AuthToken(long id, String token, int userId) {
        super(null);
        this.id = id;
        this.token = token;
        this.userId = userId;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return getToken();
    }

    @Override
    public Object getPrincipal() {
        return getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken() {
        this.token = generateToken();
    }

    public int getUserId() {
        return userId;
    }

    public String generateToken() {
        return new StringBuilder()
                .append(new Timestamp(System.currentTimeMillis()).getTime())
                .append("-")
                .append(UUID.randomUUID())
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuthToken authToken = (AuthToken) o;

        if (userId != authToken.userId) return false;
        if (id != authToken.id) return false;
        return token != null ? token.equals(authToken.token) : authToken.token == null;
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + (int) (id ^ (id >>> 32));
        result = 31 * result + (token != null ? token.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AuthToken{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", userId=" + userId +
                '}';
    }
}

