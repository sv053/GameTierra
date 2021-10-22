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
    private static Game checkingGame;
    private static int checkingGameId;

    @BeforeAll
    public static void getGamesList() {
        games = SampleData.getGames();
        checkingGameId = 0;
        checkingGame = games.get(checkingGameId);
    }

    @Test
    void testEquals() {
        Game game = new Game(checkingGame.getName(), checkingGame.getPrice());
        assertTrue(game.equals(checkingGame));
    }

    @Test
    void testHashCode() {
        Game game = new Game(checkingGame.getName(), checkingGame.getPrice());
        assertEquals(game.hashCode(), checkingGame.hashCode());
    }
}

