package com.gamesage.store.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class GameTest {

    @Test
    void testEquals_SameIdsSameNames() {
        final Game game1 = new Game(1, "THE_WITCHER", BigDecimal.valueOf(17.28d));
        final Game game2 = new Game(1, "THE_WITCHER", null);
        assertEquals(game1, game2);
    }

    @Test
    void testEquals_SameIdsDifferentNames() {
        final Game game1 = new Game(1, "THE_WITCHER", BigDecimal.valueOf(17.28d));
        final Game game2 = new Game(1, "the last of us", null);
        assertNotEquals(game1, game2);
    }

    @Test
    void testEquals_DifferentIdsSameNames() {
        final Game game1 = new Game(3, "THE_WITCHER", BigDecimal.valueOf(17.28d));
        final Game game2 = new Game(5, "THE_WITCHER", null);
        assertNotEquals(game1, game2);
    }

    @Test
    void testHashCode_SameIdsSameNames() {
        final Game game1 = new Game(1, "THE_WITCHER", BigDecimal.valueOf(17.28d));
        final Game game2 = new Game(1, "THE_WITCHER", BigDecimal.valueOf(17.28d));
        assertEquals(game1.hashCode(), game2.hashCode());
    }

    @Test
    void testHashCode_DifferentIdsSameNames() {
        final Game game1 = new Game(1, "THE_WITCHER", BigDecimal.valueOf(17.28d));
        final Game game2 = new Game(2, "THE_WITCHER", BigDecimal.valueOf(17.28d));
        assertNotEquals(game1.hashCode(), game2.hashCode());
    }

    @Test
    void testHashCode_SameIdsDifferentNames() {
        final Game game1 = new Game(1, "THE_WITCHER", BigDecimal.valueOf(17.28d));
        final Game game2 = new Game(1, "desperados", BigDecimal.valueOf(17.28d));
        assertNotEquals(game1.hashCode(), game2.hashCode());
    }
}

