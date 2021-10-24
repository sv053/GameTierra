package test.model;

import model.Game;
import model.Store;
import model.Tier;
import model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.SampleData;

import java.math.BigDecimal;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StoreTest {

    private static BigDecimal initialBalance;

    @BeforeAll
    static void init() {
        double sumOfMoney = 156.82;
        initialBalance = BigDecimal.valueOf(sumOfMoney);
    }

    @Test
    void getGameById() {
        int gameId = 3;
        Iterator<Game> iterator = SampleData.GAMES.listIterator();
        Game searchResult = null;

        while (iterator.hasNext()) {
            searchResult = iterator.next();
            if (searchResult.getId().equals(gameId)) break;
        }
        assertEquals(searchResult, Store.getInstance().getGameById(gameId));
    }

    @Test
    void buyGame() {
        int tierLvl = 4;
        Tier tier = SampleData.TIERS.get(tierLvl);
        User user = new User(tier, initialBalance);

        int gameId = 3;
        Game game = SampleData.GAMES.stream().filter(n -> n.getId() == gameId).findFirst().get();
        user = Store.getInstance().buyGame(gameId, user);

        BigDecimal cashback = game.getPrice().multiply(BigDecimal.valueOf(user.getTier().getCashbackPercentage() * 0.01));
        BigDecimal expectedBalance = initialBalance.subtract(game.getPrice()).add(cashback);

        assertEquals(expectedBalance, user.getBalance());
        assertTrue(user.hasGame(game));
    }

    @Test
    void calculateCashback() {
        int tierLvl = 3;
        Tier tier = SampleData.TIERS.get(tierLvl);
        User user = new User(tier, initialBalance);
        int gameId = 3;
        Game game = SampleData.GAMES.get(gameId);

        BigDecimal cashbackPercentage = BigDecimal.valueOf(user.getTier().getCashbackPercentage() * 0.01d);
        BigDecimal cashback = game.getPrice().multiply(cashbackPercentage);
        BigDecimal newBalance = user.getBalance().add(cashback);
        assertTrue(Store.getInstance().calculateCashback(game.getPrice(), user).compareTo(newBalance) <= 0);
        user.getBalance();
    }
}

