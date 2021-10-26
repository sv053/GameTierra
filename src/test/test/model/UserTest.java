package test.model;

import model.Game;
import model.User;
import org.junit.jupiter.api.Test;
import utils.SampleData;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void hasGame() {
        User user = new User(SampleData.TIERS.get(1), BigDecimal.valueOf(123.15d));
        user.addGame(SampleData.GAMES.get(2));

        assertTrue(user.hasGame(SampleData.GAMES.get(2)));
    }

    @Test
    void doesNotHaveGame() {
        User user = new User(SampleData.TIERS.get(1), BigDecimal.valueOf(123));
        Game alienGame = SampleData.GAMES.get(3);

        assertFalse(user.hasGame(alienGame));
    }

    @Test
    void addGame() {
        User user = new User(SampleData.TIERS.get(1), BigDecimal.valueOf(87.5));
        Game aGame = SampleData.GAMES.get(3);

        assertTrue(user.addGame(aGame));
        assertTrue(user.getGames().contains(aGame));
    }

    @Test
    void canPay() {
        BigDecimal balance = BigDecimal.valueOf(53.63);
        User user = new User(SampleData.TIERS.get(1), balance);

        assertTrue(user.canPay(balance));
    }

    @Test
    void cannotPay() {
        BigDecimal balance = BigDecimal.valueOf(123.15);
        User user = new User(SampleData.TIERS.get(4), balance);

        assertFalse(user.canPay(balance.add(BigDecimal.valueOf(1))));
    }

    @Test
    void withdrawBalance() {
        BigDecimal balance = BigDecimal.valueOf(123.15);
        BigDecimal amount = BigDecimal.valueOf(1);
        User user = new User(SampleData.TIERS.get(4), balance);

        user.withdrawBalance(amount);

        assertEquals(user.getBalance(), balance.subtract(amount));
    }

    @Test
    void depositBalance() {
        BigDecimal initialBalance = BigDecimal.valueOf(123.15);
        BigDecimal amount = BigDecimal.valueOf(1);
        User user = new User(SampleData.TIERS.get(2), initialBalance);

        user.depositBalance(amount);

        assertEquals(user.getBalance(), initialBalance.add(amount));
    }
}

