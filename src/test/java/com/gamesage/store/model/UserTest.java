package com.gamesage.store.model;

import com.gamesage.store.utility.SampleData;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void hasGame() {
        Game game = new Game(4, "SKYRIM", BigDecimal.valueOf(87.88d));
        User user = new User(SampleData.TIERS.get(1), BigDecimal.valueOf(123.15d));
        user.addGame(game);

        assertTrue(user.hasGame(game));
    }

    @Test
    void doesNotHaveGame() {
        User user = new User(SampleData.TIERS.get(1), BigDecimal.valueOf(123));
        Game alienGame = new Game(4, "SKYRIM", BigDecimal.valueOf(87.88d));

        assertFalse(user.hasGame(alienGame));
    }

    @Test
    void addGame() {
        User user = new User(SampleData.TIERS.get(1), BigDecimal.valueOf(87.5));
        Game aGame = new Game(4, "SKYRIM", BigDecimal.valueOf(87.88d));

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

        assertFalse(user.canPay(balance.add(BigDecimal.ONE)));
    }

    @Test
    void withdrawBalance() {
        BigDecimal balance = BigDecimal.valueOf(123.15);
        BigDecimal amount = BigDecimal.valueOf(1);
        User user = new User(SampleData.TIERS.get(4), balance);

        user.withdrawBalance(amount);

        assertEquals(balance.subtract(amount), user.getBalance());
    }

    @Test
    void depositBalance() {
        BigDecimal initialBalance = BigDecimal.valueOf(123.15);
        BigDecimal amount = BigDecimal.valueOf(1);
        User user = new User(SampleData.TIERS.get(2), initialBalance);

        user.depositBalance(amount);

        assertEquals(initialBalance.add(amount), user.getBalance());
    }
}

