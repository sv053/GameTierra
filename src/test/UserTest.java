package main.test;

import main.main.datagen.DataGenerator;
import main.main.model.Game;
import main.main.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    DataGenerator dataGenerator;
    User userToTest;

    private UserTest() {
        dataGenerator = new DataGenerator();
        userToTest = new User(DataGenerator.getTiers().get(4), 123.15);
        userToTest.addGameToTheUserGamesList(DataGenerator.getGames().get(1));
        userToTest.addGameToTheUserGamesList(DataGenerator.getGames().get(4));
    }

    @Test
    void ifGameIsAlreadyBought() {
        Game gameToCheck1 = DataGenerator.getGames().get(1);
        Game gameToCheck2 = new Game(3, "SKYRIM", 87.88);
        Game gameToCheck3 = DataGenerator.getGames().get(5);

        assertTrue(userToTest.ifGameIsAlreadyBought(gameToCheck1));
        assertTrue(userToTest.ifGameIsAlreadyBought(gameToCheck2));
        assertFalse(userToTest.ifGameIsAlreadyBought(gameToCheck3));
    }

    @Test
    void addGameToTheUserGamesList() {
        Game gameToCheck1 = DataGenerator.getGames().get(2);
        assertTrue(userToTest.addGameToTheUserGamesList(gameToCheck1));
        assertTrue(userToTest.getUserGames().contains(gameToCheck1));
    }

    @Test
    void addCashback() {
        double gamePrice = 63.53;
        assertTrue(userToTest.addCashback(gamePrice) >= 142.208 && userToTest.addCashback(gamePrice) <= 142.21);
    }

    @Test
    void getBalance() {
        assertEquals(123.15, userToTest.getBalance());
    }

    @Test
    void canPay() {
        double gamePriceHigherThanBalance = 163.53;
        double gamePriceLessThanBalance = 63.53;
        assertTrue(userToTest.canPay(gamePriceLessThanBalance));
        assertFalse(userToTest.canPay(gamePriceHigherThanBalance));
    }

    @Test
    void pay() {
        double gamePrice = 48.03;
        userToTest.pay(gamePrice);
        assertTrue(userToTest.getBalance() == 171.18);

    }
}

