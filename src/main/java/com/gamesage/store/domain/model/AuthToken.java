package com.gamesage.store.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class AuthToken {

    private final Integer userId;
    private final String value;

    private LocalDateTime expirationDate;

    public AuthToken(String tokenValue, Integer userId, LocalDateTime expiringDateTime) {
        this.value = tokenValue;
        this.userId = userId;
        this.expirationDate = expiringDateTime;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getValue() {
        return value;
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
        return Objects.hash(super.hashCode(), value, userId, expirationDate);
    }

    @Override
    public String toString() {
        return "AuthToken{" +
                ", token='" + value + '\'' +
                ", userId=" + userId +
                ", expirationDate=" + expirationDate +
                '}';
    }
}

