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

    @Test
    void buyGame() {
        BigDecimal initBalance = BigDecimal.valueOf(156.82);
        User user = new User(SampleData.TIERS.get(1), initBalance);
        Game game = Store.getInstance().searchGame(1);

        Store.getInstance().buyGame(1, user);

        BigDecimal cashback = game.getPrice().multiply(BigDecimal.valueOf
                (user.getTier().getCashbackPercentage() * 0.01));
        BigDecimal expectedBalance = initBalance.subtract(game.getPrice()).add(cashback)
                .setScale(2, RoundingMode.HALF_UP);

        assertEquals(expectedBalance, user.getBalance());
        assertTrue(user.hasGame(game));
    }

    @Test
    void doesNotBuyGameDoesntExist() {
        User user = new User(SampleData.TIERS.get(1), BigDecimal.valueOf(156.82));

        assertThrows(NullPointerException.class, () -> Store.getInstance().buyGame(1213313313, user));
    }

    @Test
    void doesNotBuyAlreadyHasGame() {
        BigDecimal initBalance = BigDecimal.valueOf(156.82);
        User user = new User(SampleData.TIERS.get(1), initBalance);
        Game game = Store.getInstance().searchGame(1);

        user.addGame(game);
        Store.getInstance().buyGame(game.getId(), user);

        assertEquals(user.getBalance(), initBalance);
        assertTrue(user.hasGame(game));
    }

    @Test
    void calculateCashback() {
        User user = new User(SampleData.TIERS.get(2), BigDecimal.valueOf(156.82));
        Game game = new Game(0, null, BigDecimal.valueOf(15));

        BigDecimal cashbackPercentage = BigDecimal.valueOf(user.getTier().getCashbackPercentage() * 0.01d);
        BigDecimal expectedCashback = game.getPrice().multiply(cashbackPercentage);

        assertEquals(Store.getInstance().calculateCashback(game.getPrice(), user), expectedCashback);
    }

    @Test
    void searchGame() {
        Game gameToSearch = new Game(7, "ASSASSIN_S_CREED", null);

        assertEquals(gameToSearch, Store.getInstance().searchGame(7));
    }
}

