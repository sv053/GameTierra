package test.model;

import model.Game;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class GameTest {

    @Test
    void testEquals() {
        Game game1 = new Game(1, "THE_WITCHER", BigDecimal.valueOf(17.28d));
        Game game2 = new Game(1, "THE_WITCHER", null);
        assertEquals(game1, game2);
    }

    @Test
    void testHashCode() {
        Game game1 = new Game(1, "THE_WITCHER", BigDecimal.valueOf(17.28d));
        Game game2 = new Game(1, "THE_WITCHER", BigDecimal.valueOf(17.28d));
        assertEquals(game1.hashCode(), game2.hashCode());
    }

    @Test
    void testUnequals() {
        Game game1 = new Game(1, "THE_WITCHER", BigDecimal.valueOf(17.28d));
        Game game2 = new Game(1, "test", null);
        assertNotEquals(game1, game2);
    }

    @Test
    void testDiffHashCode() {
        Game game1 = new Game(1, "THE_WITCHER", BigDecimal.valueOf(17.28d));
        Game game2 = new Game(1, "test", BigDecimal.valueOf(17.28d));
        Game game3 = new Game(2, "THE_WITCHER", BigDecimal.valueOf(17.28d));
        assertNotEquals(game1.hashCode(), game2.hashCode());
        assertNotEquals(game1.hashCode(), game3.hashCode());
    }
}

