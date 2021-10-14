package main.test;

import main.main.datagen.DataGenerator;
import main.main.datagen.Strings;
import main.main.model.Game;
import main.main.model.Store;
import main.main.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StoreTest {

    User user;
    Store store;

    public StoreTest() {
        user = new User(DataGenerator.getTiers().get(4), 123.15);
        user.addGameToTheUserGamesList(DataGenerator.getGames().get(1));
        user.addGameToTheUserGamesList(DataGenerator.getGames().get(4));
        store = new Store(user);
    }

    @Test
    void getGameByIndex() {
        Game gameToSearch = DataGenerator.getGames().get(1);

        assertEquals(gameToSearch, store.getGameByIndex(gameToSearch.getGameNumber()));
    }

    @Test
    void buyGame() {
        int gameNumber = 4;

        assertEquals(Strings.CONGRATS.getMsg(), store.buyGame(gameNumber));
        assertTrue(store.getUser().getBalance() < 55);
        assertTrue(user.ifGameIsAlreadyBought(DataGenerator.getGames().get(gameNumber)));

        gameNumber = 6;
        assertEquals(Strings.NOT_ENOUGH_MONEY.getMsg(), store.buyGame(gameNumber));
    }

    @Test
    void checkIfHasGame() {
        assertEquals(Strings.ALREADY_BOUGHT.getMsg(), store.checkIfHasGame(DataGenerator.getGames().get(1)));
        assertEquals("", store.checkIfHasGame(DataGenerator.getGames().get(7)));
    }
}



