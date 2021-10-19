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
        Game gameToCheck = SampleData.getGames().get(1);

        assertTrue(userToTest.hasGame(gameToCheck));
    }


    @Test
    void doesNotHaveGame() {
        Game gameToCheck = SampleData.getGames().get(5);

        assertFalse(userToTest.hasGame(gameToCheck));
    }

    @Test
    void addGame() {
        Game gameToCheck = SampleData.getGames().get(2);
        assertTrue(userToTest.addToOwnedGames(gameToCheck));
        assertTrue(userToTest.getGames().contains(gameToCheck));
    }

    @Test
    void canPay() {
        BigDecimal gamePriceLessThanBalance = BigDecimal.valueOf(63.53d);
        assertTrue(userToTest.canPay(gamePriceLessThanBalance));
    }

    @Test
    void cannotPay() {
        BigDecimal gamePriceHigherThanBalance = BigDecimal.valueOf(163.53d);
        assertFalse(userToTest.canPay(gamePriceHigherThanBalance));
    }

    @Test
    void pay() {
        BigDecimal gamePrice = BigDecimal.valueOf(48.03d);
        userToTest.pay(gamePrice);
        assertTrue(userToTest.getBalance().compareTo(BigDecimal.valueOf(75.12)) <= 0);
    }
}
