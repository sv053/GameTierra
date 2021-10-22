package test.model;

import model.Game;
import model.Store;
import model.Tier;
import model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.SampleData;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StoreTest {

    private static User user;
    private static Store store;
    private static Game game;
    private static final int ownedGameId1 = 1;
    private static final int ownedGameId2 = 2;
    private static int gameId;
    private static BigDecimal initialBalance;

    @BeforeAll
    public static void createUserAndStore() {

        int tierLvl = 4;
        int multForRandom = 100;
        double account = new Random().nextDouble() * multForRandom;
        gameId = new Random().nextInt(SampleData.getGames().size() - 1);
        initialBalance = BigDecimal.valueOf(account);
        Tier tier = SampleData.getTiers().get(tierLvl);
        user = new User(tier, initialBalance);

        user.addToOwnedGames(SampleData.getGames().get(ownedGameId1));
        user.addToOwnedGames(SampleData.getGames().get(ownedGameId2));
        store = Store.getInstance();
        game = SampleData.getGames().get(gameId);
    }

    @Test
    public void getGameById() {
        assertEquals(game, store.getGameById(game.getId()));
    }

    @Test
    public void buyGame() {
        User userToCheck = store.buyGame(game.getId(), user);
        if (game.getPrice().compareTo(initialBalance) <= 0) {
            BigDecimal cashback = store.calculateCashback(game.getPrice(), userToCheck);
            BigDecimal bal = initialBalance.subtract(game.getPrice()).add(cashback);
            assertEquals(userToCheck.getBalance(), bal);
            assertTrue(userToCheck.hasGame(game));
        } else {
            System.out.println("The price is higher than your balance " + game.getPrice() + " : " + userToCheck.getBalance());
        }
    }

    @Test
    public void addCashback() {
        BigDecimal cashbackPercentage = BigDecimal.valueOf(user.getTier().getCashbackPercentage() * 0.01d);
        BigDecimal cashback = game.getPrice().multiply(cashbackPercentage);
        BigDecimal newBalance = user.getBalance().add(cashback);
        assertTrue(store.calculateCashback(game.getPrice(), user).compareTo(newBalance) <= 0);
    }

    @BeforeEach
    public void restoreState() {
        if (user.getBalance().compareTo(initialBalance) != 0)
            user.withdrawBalance(initialBalance.subtract(user.getBalance()));

        List<Game> ownedGames = user.getGames();
        if (ownedGames.size() > 2) {
            ownedGames.clear();
            ownedGames.add(SampleData.getGames().get(ownedGameId1));
            ownedGames.add(SampleData.getGames().get(ownedGameId2));
        }
    }
}

