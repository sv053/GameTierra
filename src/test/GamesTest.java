package test;

import app.model.Game;
import app.model.Games;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GamesTest {

    private List<Game> createGameList(){
        return  Arrays.asList(new Game(1, "gameName1", "desc1", true, 12.56),
                new Game(2, "gameName2", "desc2", true, 18.17),
                new Game(3, "gameName3", "desc3", true, 115.02));
    }

    @Test
    void getGame() {
        Game okGame = new Game(3, "gameName3", "desc3", true, 115.02);
        assertEquals(okGame, createGameList().stream().filter(g -> g.equals(okGame)).findFirst().get());

        Game falseGame1 = new Game(1, "gameName3", "desc3", true, 115.02);
        Game falseGame2 = new Game(1, "gameName1", "desc3", true, 1.02);

        Exception exception = assertThrows(NoSuchElementException.class, () ->
                Games.getGame(falseGame1));
        assertEquals("No value present", exception.getMessage());

        Exception exception2 = assertThrows(NoSuchElementException.class, () ->
                Games.getGame(falseGame2));
        assertEquals("No value present", exception2.getMessage());
    }

    @Test
    void testGetGameByIndex() {
        Game okGame = new Game(3, "gameName3", "desc3", true, 115.02);
        assertEquals(okGame, createGameList().stream().filter(g -> g.getGameNumber().equals(okGame.getGameNumber())).findFirst().get());
    }
}