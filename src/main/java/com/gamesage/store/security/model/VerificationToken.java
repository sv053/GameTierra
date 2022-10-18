package com.gamesage.store.security.model;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

public class VerificationToken {

    private final int userId;
    private long id;
    private String token;

    public VerificationToken(int userId) {
        this.token = generateToken();
        this.userId = userId;
    }

    public VerificationToken(long id, String token, int userId) {
        this.id = id;
        this.token = token;
        this.userId = userId;
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
        StringBuilder token = new StringBuilder();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        long currentTimeInMilisecond = timestamp.getTime();
        return token
                .append(currentTimeInMilisecond)
                .append("-")
                .append(UUID.randomUUID())
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VerificationToken that = (VerificationToken) o;

        if (!Objects.equals(token, that.token)) return false;
        if (userId < 1) return false;
        return false;
    }

    @Override
    public int hashCode() {
        int result = id != 0 ? 5 : 0;
        result = 31 * result + (token != null ? token.hashCode() : 0);
        result = 31 * result + userId;
        return result;
    }

    @Override
    public String toString() {
        return "VerificationToken{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", userId=" + userId +
                '}';
    }
}

