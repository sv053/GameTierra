package com.gamesage.store.domain.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(classes = GameTest.class)
class GameTest {

    Logger logger = Logger.getLogger(String.valueOf(GameTest.class));
    Logger logger1 = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Test
    void testEquals_SameIdsSameNames() {
        Game game1 = new Game(1, "THE_WITCHER", BigDecimal.valueOf(17.28d));
        Game game2 = new Game(1, "THE_WITCHER", null);
        assertEquals(game1, game2);
    }

    @Test
    void testEquals_SameIdsDifferentNames() {
        Game game1 = new Game(1, "THE_WITCHER", BigDecimal.valueOf(17.28d));
        Game game2 = new Game(1, "the last of us", null);
        assertNotEquals(game1, game2);
    }

    @Test
    void testEquals_DifferentIdsSameNames() {
        Game game1 = new Game(3, "THE_WITCHER", BigDecimal.valueOf(17.28d));
        Game game2 = new Game(5, "THE_WITCHER", null);
        assertNotEquals(game1, game2);
    }

    @Test
    void testHashCode_SameIdsSameNames() {
        Game game1 = new Game(1, "THE_WITCHER", BigDecimal.valueOf(17.28d));
        Game game2 = new Game(1, "THE_WITCHER", BigDecimal.valueOf(17.28d));
        assertEquals(game1.hashCode(), game2.hashCode());
    }

    @Test
    void testHashCode_DifferentIdsSameNames() {
        Game game1 = new Game(1, "THE_WITCHER", BigDecimal.valueOf(17.28d));
        Game game2 = new Game(2, "THE_WITCHER", BigDecimal.valueOf(17.28d));
        assertNotEquals(game1.hashCode(), game2.hashCode());
    }

    @Test
    void testHashCode_SameIdsDifferentNames() {
        Game game1 = new Game(1, "THE_WITCHER", BigDecimal.valueOf(17.28d));
        Game game2 = new Game(1, "desperados", BigDecimal.valueOf(17.28d));
        assertNotEquals(game1.hashCode(), game2.hashCode());
    }
}

