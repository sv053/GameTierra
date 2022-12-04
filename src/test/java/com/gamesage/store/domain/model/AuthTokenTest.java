package com.gamesage.store.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class AuthTokenTest {

    @Test
    void equals_SameValuesSameLogins_True() {
        AuthToken token1 = new AuthToken("enkey", "costa");
        AuthToken token2 = new AuthToken("enkey", "costa");

        assertEquals(token1, token2);
    }

    @Test
    void equals_SameValuesDiffLogins_False() {
        AuthToken token1 = new AuthToken("enkey", "costa");
        AuthToken token2 = new AuthToken("enkey", "diffcostasol");

        assertNotEquals(token1, token2);
    }

    @Test
    void equals_DiffValuesSameLogins_False() {
        AuthToken token1 = new AuthToken("key", "costa");
        AuthToken token2 = new AuthToken("diffkey", "costa");

        assertNotEquals(token1, token2);
    }

    @Test
    void equals_DiffValuesDiffLogins_False() {
        AuthToken token1 = new AuthToken("key", "costa");
        AuthToken token2 = new AuthToken("diffkey", "diffcosta");

        assertNotEquals(token1, token2);
    }
}

