package test.model;

import model.Game;
import model.Store;
import model.User;
import org.junit.jupiter.api.Test;
import utils.SampleData;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StoreTest {

    @Test
    void buyGame() {
        Store.getInstance().setGames();
        BigDecimal initBalance = BigDecimal.valueOf(156.82);
        User user = new User(SampleData.TIERS.get(1), initBalance);
        Game game = Store.getInstance().getGames().get(1);

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
        Store.getInstance().setGames();
        BigDecimal initBalance = BigDecimal.valueOf(156.82);
        User user = new User(SampleData.TIERS.get(1), initBalance);

        assertEquals(Store.getInstance().buyGame(11, user).getBalance(), initBalance);
    }

    @Test
    void doesNotBuyAlreadyHasGame() {
        Store.getInstance().setGames();
        BigDecimal initBalance = BigDecimal.valueOf(156.82);
        User user = new User(SampleData.TIERS.get(1), initBalance);
        Game game = Store.getInstance().getGames().get(1);

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
}

