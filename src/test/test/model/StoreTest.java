package test.model;

import model.Game;
import model.Store;
import model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.SampleData;
import utils.UI_Phrases;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StoreTest {

    private static User user;
    private static Store store;
    private static Game game;

    @BeforeAll
    public static void createUserAndStore() {
        user = new User(SampleData.getTiers().get(4), BigDecimal.valueOf(123.15d));
        user.addToOwnedGames(SampleData.getGames().get(1));
        user.addToOwnedGames(SampleData.getGames().get(4));
        store = Store.getInstance();
        game = SampleData.getGames().get(2);

        System.out.println(UI_Phrases.WELCOME.getPhrase() + user + UI_Phrases.SHOWCASE.getPhrase() + SampleData.getGames());
    }

    @Test
    public void getGameByIndex() {
        assertEquals(game, store.getGameByIndex(3));

        System.out.println(UI_Phrases.CHOICE_IS.getPhrase() + game);
    }

    @Test
    void buyGame() {
        User userToCheck = store.buyGame(game.getId(), user);

        assertTrue(userToCheck.getBalance().compareTo(BigDecimal.valueOf(123.15d)) <= 0);
        assertTrue(userToCheck.hasGame(game));
        System.out.println(userToCheck);
    }

    @Test
    void addCashback() {
        BigDecimal gamePrice = BigDecimal.valueOf(63.53);
        assertTrue(store.addCashback(gamePrice, user).compareTo(BigDecimal.valueOf(142.21d)) <= .31);
    }
}
