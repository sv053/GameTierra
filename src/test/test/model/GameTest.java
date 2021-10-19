package test.model;

import model.Game;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.SampleData;

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
        Game game = new Game(games.get(0).getName(), games.get(0).getPrice());
        Game gameToCompare = new Game(games.get(0).getName(), games.get(0).getPrice());
        assertTrue(game.equals(gameToCompare));
    }

    @Test
    void testHashCode() {
        Game game = new Game(games.get(0).getName(), games.get(0).getPrice());
        Game gameToCompare = new Game(games.get(0).getName(), games.get(0).getPrice());
        assertEquals(game.hashCode(), gameToCompare.hashCode());
    }
}

