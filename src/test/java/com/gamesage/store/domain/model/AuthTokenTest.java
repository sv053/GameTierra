package com.gamesage.store.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class AuthTokenTest {

    @Test
    void equals_SameValuesSameIds_True() {
        AuthToken token1 = new AuthToken("enkey", 1);
        AuthToken token2 = new AuthToken("enkey", 1);

        assertEquals(token1, token2);
    }

    @Test
    void equals_SameValuesDiffIds_False() {
        AuthToken token1 = new AuthToken("enkey", 7);
        AuthToken token2 = new AuthToken("enkey", 77);

        assertNotEquals(token1, token2);
    }

    @Test
    void equals_DiffValuesSameIds_False() {
        AuthToken token1 = new AuthToken("key", 8);
        AuthToken token2 = new AuthToken("diffkey", 8);

        assertNotEquals(token1, token2);
    }

    @Test
    void equals_DiffValuesDiffIds_False() {
        AuthToken token1 = new AuthToken("key", 999);
        AuthToken token2 = new AuthToken("diffkey", -999);

        assertNotEquals(token1, token2);
    }
}

