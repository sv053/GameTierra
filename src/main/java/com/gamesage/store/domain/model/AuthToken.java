package com.gamesage.store.domain.model;

import java.util.Objects;

public class AuthToken {

    private final String userLogin;
    private final String value;

    public AuthToken(String tokenValue, String userLogin) {
        this.value = tokenValue;
        this.userLogin = userLogin;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthToken thatToken = (AuthToken) o;
        return Objects.equals(userLogin, thatToken.getUserLogin())
                && Objects.equals(value, thatToken.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value);
    }

    @Override
    public String toString() {
        return "AuthToken{" +
                ", token='" + value + '\'' +
                ", userLogin=" + userLogin +
                '}';
    }
}

