package test.model;

import model.Game;
import org.junit.jupiter.api.Test;
import utils.SampleData;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameTest {

    @Test
    void testEquals() {
        int index = 0;
        Game game1 = SampleData.GAMES.get(index);
        Game game2 = new Game(game1.getId(), game1.getName(), game1.getPrice());
        assertEquals(game1, game2);
    }

    @Test
    void testHashCode() {
        int index = 0;
        Game game1 = SampleData.GAMES.get(index);
        Game game2 = new Game(game1.getId(), game1.getName(), game1.getPrice());
        assertEquals(game1.hashCode(), game2.hashCode());
    }
}

