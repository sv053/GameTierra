package com.gamesage.store.security.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class AuthToken {

    private final String userLogin;
    @JsonProperty
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
        if (!super.equals(o)) return false;
        AuthToken thatToken = (AuthToken) o;
        return Objects.equals(userLogin, thatToken.getUserLogin());
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

