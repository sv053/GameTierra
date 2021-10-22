package test.model;

import model.Game;
import model.Tier;
import model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.SampleData;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {

    private static final int ownedGameId1 = 1;
    private static final int ownedGameId2 = 4;
    private static final int gameId = 5;
    private static User user;
    private static Game ownedGame;
    private static Game game;
    private static BigDecimal initialBalance;
    private static BigDecimal dif;

    @BeforeAll
    public static void createTestUser() {
        int tierLvl = 4;
        double account = 123.15d;
        dif = BigDecimal.valueOf(account * .1d);
        initialBalance = BigDecimal.valueOf(account);
        Tier tier = SampleData.getTiers().get(tierLvl);
        user = new User(tier, initialBalance);
        user.addToOwnedGames(SampleData.getGames().get(ownedGameId1));
        user.addToOwnedGames(SampleData.getGames().get(ownedGameId2));
        ownedGame = SampleData.getGames().get(ownedGameId2);
        game = SampleData.getGames().get(gameId);
    }

    @Test
    public void hasGame() {
        assertTrue(user.hasGame(ownedGame));
    }

    @Test
    public void doesNotHaveGame() {
        assertFalse(user.hasGame(game));
    }

    @Test
    public void addGame() {
        assertTrue(user.addToOwnedGames(game));
        assertTrue(user.getGames().contains(game));
    }

    @Test
    public void canPay() {
        BigDecimal gamePriceLessThanBalance = initialBalance.subtract(dif);
        assertTrue(user.canPay(gamePriceLessThanBalance));
    }

    @Test
    public void cannotPay() {
        BigDecimal gamePriceMoreThanBalance = initialBalance.add(dif);
        assertFalse(user.canPay(gamePriceMoreThanBalance));
    }

    @Test
    public void withdrawBalance() {
        if (initialBalance.compareTo(game.getPrice()) >= 0) {
            user.withdrawBalance(game.getPrice());
            BigDecimal restOfBalance = initialBalance.subtract(game.getPrice());
            assertTrue(user.getBalance().equals(restOfBalance));
        }
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

