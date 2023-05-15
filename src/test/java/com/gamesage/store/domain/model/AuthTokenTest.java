package com.gamesage.store.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class AuthTokenTest {

    @Test
    void equals_SameValuesSameIds_True() {
        LocalDateTime now = LocalDateTime.now();

        AuthToken token1 = new AuthToken("enkey", 1, now);
        AuthToken token2 = new AuthToken("enkey", 1, now);

        assertEquals(token1, token2);
    }

    @Test
    void equals_SameValuesDiffIds_False() {
        LocalDateTime now = LocalDateTime.now();

        AuthToken token1 = new AuthToken("enkey", 7, now);
        AuthToken token2 = new AuthToken("enkey", 77, now);

        assertNotEquals(token1, token2);
    }

    @Test
    void equals_DiffValuesSameIds_False() {
        LocalDateTime now = LocalDateTime.now();

        AuthToken token1 = new AuthToken("key", 8, now);
        AuthToken token2 = new AuthToken("diffkey", 8, now);

        assertNotEquals(token1, token2);
    }

    @Test
    void equals_DiffValuesDiffIds_False() {
        LocalDateTime now = LocalDateTime.now();
        AuthToken token1 = new AuthToken("key", 999, now);
        AuthToken token2 = new AuthToken("diffkey", -999, now);

        assertNotEquals(token1, token2);
    }
}

