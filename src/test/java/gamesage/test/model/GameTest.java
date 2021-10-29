package gamesage.test.model;

import gamesage.model.Game;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class GameTest {

    @Test
    void testEqualsWithSameIdsSameNames() {
        Game game1 = new Game(1, "THE_WITCHER", BigDecimal.valueOf(17.28d));
        Game game2 = new Game(1, "THE_WITCHER", null);
        assertEquals(game1, game2);
    }

    @Test
    void testEqualsWithSameIdsDifferentNames() {
        Game game1 = new Game(1, "THE_WITCHER", BigDecimal.valueOf(17.28d));
        Game game2 = new Game(1, "games/age/test", null);
        assertNotEquals(game1, game2);
    }

    @Test
    void testEqualsWithDifferentIdsSameNames() {
        Game game1 = new Game(3, "THE_WITCHER", BigDecimal.valueOf(17.28d));
        Game game2 = new Game(5, "THE_WITCHER", null);
        assertNotEquals(game1, game2);
    }

    @Test
    void testHashCodeWithSameIdsSameNames() {
        Game game1 = new Game(1, "THE_WITCHER", BigDecimal.valueOf(17.28d));
        Game game2 = new Game(1, "THE_WITCHER", BigDecimal.valueOf(17.28d));
        assertEquals(game1.hashCode(), game2.hashCode());
    }

    @Test
    void testHashCodeWithDifferentIdsSameNames() {
        Game game1 = new Game(1, "THE_WITCHER", BigDecimal.valueOf(17.28d));
        Game game2 = new Game(2, "THE_WITCHER", BigDecimal.valueOf(17.28d));
        assertNotEquals(game1.hashCode(), game2.hashCode());
    }

    @Test
    void testHashCodeWithSameIdsDifferentNames() {
        Game game1 = new Game(1, "THE_WITCHER", BigDecimal.valueOf(17.28d));
        Game game2 = new Game(1, "games/age/test", BigDecimal.valueOf(17.28d));
        assertNotEquals(game1.hashCode(), game2.hashCode());
    }
}

