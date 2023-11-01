package com.gamesage.store.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class AuthToken {

    private final Integer id;
    private final Integer userId;
    private final String value;
    private final LocalDateTime expirationDateTime;

    public AuthToken(Integer userId) {
        this(null, null, userId, null);
    }

    public AuthToken(String tokenValue, Integer userId) {
        this(null, tokenValue, userId, null);
    }

    public AuthToken(Integer userId, LocalDateTime expirationDateTime) {
        this(null, userId, expirationDateTime);
    }

    public AuthToken(String tokenValue, Integer userId, LocalDateTime expirationDateTime) {
        this(null, tokenValue, userId, expirationDateTime);
    }

    public AuthToken(Integer id, String tokenValue, Integer userId, LocalDateTime expirationDateTime) {
        this.id = id;
        this.value = tokenValue;
        this.userId = userId;
        this.expirationDateTime = expirationDateTime;
    }

    public Integer getId() {
        return id;
    }

    public LocalDateTime getExpirationDateTime() {
        return expirationDateTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getValue() {
        return value;
    }

    public AuthToken withTokenValue(String token) {
        return new AuthToken(id, token, userId, expirationDateTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthToken thatToken = (AuthToken) o;
        return Objects.equals(userId, thatToken.getUserId())
                && Objects.equals(value, thatToken.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value, userId, expirationDateTime);
    }

    @Override
    public String toString() {
        return "AuthToken{" +
                ", id='" + id + '\'' +
                ", token='" + value + '\'' +
                ", userId=" + userId +
                ", expirationDateTime=" + expirationDateTime +
                '}';
    }
}

