package test.model;

import model.Game;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.SampleData;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameTest {

    private static List<Game> games;

    @BeforeAll
    public static void getGamesList() {
        games = SampleData.getGames();
    }

    @Test
    void testEquals() {
        Game gameToCompare = new Game("SKYRIM", BigDecimal.valueOf(87.88));
        assertTrue(games.get(3).equals(gameToCompare));
    }

    @Test
    void testHashCode() {
        Game gameToCompare = new Game("SKYRIM", BigDecimal.valueOf(87.88));
        assertEquals(games.get(3).hashCode(), gameToCompare.hashCode());
    }
}

