package com.gamesage.store.domain.repository.model;

import java.util.Objects;

public class AuthTokenEntity {

    private final int userId;
    private final String value;

    public AuthTokenEntity(int userId, String value) {
        this.userId = userId;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuthTokenEntity authTokenEntity = (AuthTokenEntity) o;

        if (userId != authTokenEntity.userId) return false;
        return Objects.equals(value, authTokenEntity.value);
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AuthTokenEntity{" +
                ", token='" + value + '\'' +
                ", userId=" + userId +
                '}';
    }
}

