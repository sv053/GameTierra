package test.model;

import model.Game;
import model.Tier;
import model.User;
import org.junit.jupiter.api.Test;
import utils.SampleData;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {

    @Test
    void hasGame() {
        int tierLvl = 4;
        Tier tier = SampleData.TIERS.get(tierLvl);
        double sumOfMoney = 123.15d;
        BigDecimal dif = BigDecimal.valueOf(sumOfMoney * .1d);
        BigDecimal initialBalance = BigDecimal.valueOf(sumOfMoney);
        User user = new User(tier, initialBalance);
        int ownedGameId1 = 1;
        int ownedGameId2 = 4;
        user.addToOwnedGames(SampleData.GAMES.get(ownedGameId1));
        user.addToOwnedGames(SampleData.GAMES.get(ownedGameId2));
        Game ownedGame = SampleData.GAMES.get(ownedGameId2);
        assertTrue(user.hasGame(ownedGame));
    }

    @Test
    void doesNotHaveGame() {
        int tierLvl = 4;
        Tier tier = SampleData.TIERS.get(tierLvl);
        double sumOfMoney = 123.15d;
        BigDecimal dif = BigDecimal.valueOf(sumOfMoney * .1d);
        BigDecimal initialBalance = BigDecimal.valueOf(sumOfMoney);
        User user = new User(tier, initialBalance);
        int ownedGameId1 = 1;
        int ownedGameId2 = 4;
        user.addToOwnedGames(SampleData.GAMES.get(ownedGameId1));
        user.addToOwnedGames(SampleData.GAMES.get(ownedGameId2));
        int numberOfNotOwnedGame = 3;
        Game alienGame = SampleData.GAMES.get(numberOfNotOwnedGame);
        assertFalse(user.hasGame(alienGame));
    }

    @Test
    void addGame() {
        int tierLvl = 4;
        Tier tier = SampleData.TIERS.get(tierLvl);
        double sumOfMoney = 123.15d;
        BigDecimal dif = BigDecimal.valueOf(sumOfMoney * .1d);
        BigDecimal initialBalance = BigDecimal.valueOf(sumOfMoney);
        User user = new User(tier, initialBalance);
        int numberOfNotOwnedGameId = 3;
        Game alienGame = SampleData.GAMES.get(numberOfNotOwnedGameId);
        assertTrue(user.addToOwnedGames(alienGame));
        assertTrue(user.getGames().contains(alienGame));
    }

    @Test
    void canPay() {
        int tierLvl = 4;
        Tier tier = SampleData.TIERS.get(tierLvl);
        double sumOfMoney = 123.15d;
        BigDecimal dif = BigDecimal.valueOf(sumOfMoney * .1d);
        BigDecimal initialBalance = BigDecimal.valueOf(sumOfMoney);
        User user = new User(tier, initialBalance);
        BigDecimal gamePriceLessThanBalance = initialBalance.subtract(dif);
        assertTrue(user.canPay(gamePriceLessThanBalance));
    }

    @Test
    void cannotPay() {
        int tierLvl = 4;
        Tier tier = SampleData.TIERS.get(tierLvl);
        double sumOfMoney = 123.15d;
        BigDecimal dif = BigDecimal.valueOf(sumOfMoney * .1d);
        BigDecimal initialBalance = BigDecimal.valueOf(sumOfMoney);
        User user = new User(tier, initialBalance);
        BigDecimal gamePriceMoreThanBalance = initialBalance.add(dif);
        assertFalse(user.canPay(gamePriceMoreThanBalance));
    }

    @Test
    void withdrawBalance() {
        int tierLvl = 4;
        Tier tier = SampleData.TIERS.get(tierLvl);
        double sumOfMoney = 123.15d;
        BigDecimal initialBalance = BigDecimal.valueOf(sumOfMoney);
        User user = new User(tier, initialBalance);
        int numberOfNotOwnedGame = 3;
        Game aGame = SampleData.GAMES.get(numberOfNotOwnedGame);
        user.withdrawBalance(aGame.getPrice());
        BigDecimal restOfBalance = initialBalance.subtract(aGame.getPrice());
        assertTrue(user.getBalance().equals(restOfBalance));
    }
}

