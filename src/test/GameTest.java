package main.test;

import main.main.datagen.DataGenerator;
import main.main.model.Game;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameTest {

    private static List<Game> games;

    @BeforeAll
    public static void getGamesList() {
        games = DataGenerator.getGames();
    }

    @Test
    void getGameNumber() {
        assertEquals(1, games.get(0).getGameNumber());
        assertEquals(3, games.get(2).getGameNumber());
        assertEquals(5, games.get(4).getGameNumber());
    }

    @Test
    void getName() {
        assertEquals("THE_WITCHER", games.get(0).getName());
        assertEquals("ASSASSIN_S_CREED", games.get(6).getName());
    }

    @Test
    void getPrice() {
        assertEquals(17.28, games.get(0).getPrice());
    }

    @Test
    void testEquals() {
        Game gameToCompare = new Game(4, "SKYRIM", 87.88);
        assertTrue(games.get(3).equals(gameToCompare));
    }

    @Test
    void testHashCode() {
        Game gameToCompare = new Game(4, "SKYRIM", 87.88);
        assertEquals(games.get(3).hashCode(), gameToCompare.hashCode());
    }
}



