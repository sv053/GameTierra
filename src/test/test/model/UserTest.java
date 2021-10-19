package test.model;

import model.Game;
import model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.SampleData;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {

    private static User userToTest;

    @BeforeAll
    public static void createTestUser() {
        userToTest = new User(SampleData.getTiers().get(4), BigDecimal.valueOf(123.15d));
        userToTest.addToOwnedGames(SampleData.getGames().get(1));
        userToTest.addToOwnedGames(SampleData.getGames().get(4));
    }

    @Test
    void hasGame() {
        Game gameToCheck1 = SampleData.getGames().get(1);
        Game gameToCheck2 = new Game("SKYRIM", BigDecimal.valueOf(87.88d));
        Game gameToCheck3 = SampleData.getGames().get(5);

        assertTrue(userToTest.hasGame(gameToCheck1));
        assertFalse(userToTest.hasGame(gameToCheck2));
        assertFalse(userToTest.hasGame(gameToCheck3));
    }

    @Test
    void addGame() {
        Game gameToCheck1 = SampleData.getGames().get(2);
        assertTrue(userToTest.addToOwnedGames(gameToCheck1));
        assertTrue(userToTest.getGames().contains(gameToCheck1));
    }

    @Test
    void canPay() {
        BigDecimal gamePriceHigherThanBalance = BigDecimal.valueOf(163.53d);
        BigDecimal gamePriceLessThanBalance = BigDecimal.valueOf(63.53d);
        assertTrue(userToTest.canPay(gamePriceLessThanBalance));
        assertFalse(userToTest.canPay(gamePriceHigherThanBalance));
    }

    @Test
    void pay() {
        BigDecimal gamePrice = BigDecimal.valueOf(48.03d);
        userToTest.pay(gamePrice);
        assertTrue(userToTest.getBalance().compareTo(BigDecimal.valueOf(75.12)) <= 0);
    }
}
