package main.test;

import main.main.datagen.DataGenerator;
import main.main.datagen.Strings;
import main.main.model.Game;
import main.main.model.Store;
import main.main.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StoreTest {

    private static User user;
    private static Store store;
    private static Game game;
    private static int gameNumber;

    @BeforeAll
    public static void createUserAndStore() {
        user = new User(DataGenerator.getTiers().get(4), 123.15);
        user.addGameToTheUserGamesList(DataGenerator.getGames().get(1));
        user.addGameToTheUserGamesList(DataGenerator.getGames().get(4));
        store = Store.getInstance(user);
        gameNumber = 2;// Math.abs(new Random().nextInt(DataGenerator.getGames().size()-1));
        game = DataGenerator.getGames().get(gameNumber);

        System.out.println(Strings.WELCOME.getMsg() + user + user.getUserGames() + Strings.SHOWCASE.getMsg() + DataGenerator.getGames());
    }

    @Test
    public void getGameByIndex() {
        assertEquals(game, store.getGameByIndex(gameNumber + 1));

        System.out.println(Strings.CHOICE_IS.getMsg() + game);
    }

    @Test
    void checkIfHasGame() {
        assertEquals(Strings.ALREADY_BOUGHT.getMsg(), store.checkIfHasGame(DataGenerator.getGames().get(1)));
        assertEquals("", store.checkIfHasGame(DataGenerator.getGames().get(7)));
    }

    @Test
    void buyGame() {
        String intentResult = store.buyGame(gameNumber);
        assertEquals(Strings.CONGRATS.getMsg(), intentResult);
        assertTrue(store.getUser().getBalance() < 123.15);
        assertFalse(user.ifGameIsAlreadyBought(DataGenerator.getGames().get(gameNumber)));

        System.out.println(intentResult + Strings.BALANCE_WITH_CASHBACK.getMsg() + user.getBalance());

        gameNumber = 6;
        assertEquals(Strings.NOT_ENOUGH_MONEY.getMsg(), store.buyGame(gameNumber));
    }
}





