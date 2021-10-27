package test.model;

import model.Game;
import model.Store;
import model.User;
import org.junit.jupiter.api.Test;
import utility.SampleData;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

public class StoreTest {
    private final Store store = Store.getInstance();

    @Test
    void buyGame() {
        BigDecimal initBalance = BigDecimal.valueOf(156.82);
        User user = new User(SampleData.TIERS.get(1), initBalance);
        Game game = store.searchGame(1);

        store.buyGame(1, user);

        BigDecimal cashback = game.getPrice().multiply(BigDecimal.valueOf
                (user.getTier().getCashbackPercentage() * 0.01));
        BigDecimal expectedBalance = initBalance.subtract(game.getPrice()).add(cashback)
                .setScale(2, RoundingMode.HALF_UP);

        assertEquals(expectedBalance, user.getBalance());
        assertTrue(user.hasGame(game));
    }

    @Test
    void gameDoesntExist() {
        assertThrows(IllegalArgumentException.class, () -> store.searchGame(1213313313));
    }

    @Test
    void doesNotBuyAlreadyOwned() {
        BigDecimal initBalance = BigDecimal.valueOf(156.82);
        User user = new User(SampleData.TIERS.get(1), initBalance);
        Game game = store.searchGame(1);

        user.addGame(game);
        store.buyGame(game.getId(), user);

        assertEquals(initBalance, user.getBalance());
        assertTrue(user.hasGame(game));
    }

    @Test
    void calculateCashback() {
        User user = new User(SampleData.TIERS.get(2), BigDecimal.valueOf(156.82));
        Game game = new Game(0, null, BigDecimal.valueOf(15));

        BigDecimal cashbackPercentage = BigDecimal.valueOf(user.getTier().getCashbackPercentage() * 0.01d);
        BigDecimal expectedCashback = game.getPrice().multiply(cashbackPercentage);

        assertEquals(expectedCashback, store.calculateCashback(game.getPrice(), user));
    }

    @Test
    void searchGame() {
        Game gameToSearch = new Game(7, "ASSASSIN_S_CREED", null);

        assertEquals(gameToSearch, store.searchGame(7));
    }
}

