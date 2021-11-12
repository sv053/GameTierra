package com.gamesage.store.domain.model;

import com.gamesage.store.domain.data.sample.SampleData;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void addGame() {
        final User user = new User(SampleData.TIERS.get(1), BigDecimal.valueOf(87.5));
        final Game game = new Game(4, "SKYRIM", BigDecimal.valueOf(87.88d));

        assertTrue(user.addGame(game));
        assertTrue(user.getGames().contains(game));
    }

    @Test
    void withdrawBalance() {
        final BigDecimal balance = BigDecimal.valueOf(123.15);
        final User user = new User(SampleData.TIERS.get(4), balance);
        user.withdrawBalance(BigDecimal.ONE);
        assertEquals(user.withdrawBalance(BigDecimal.ONE), user.getBalance());
    }

    @Test
    void depositBalance() {
        final BigDecimal initialBalance = BigDecimal.valueOf(123.15);
        final User user = new User(SampleData.TIERS.get(2), initialBalance);
        user.depositBalance(BigDecimal.ONE);
        assertEquals(user.getBalance(), initialBalance.add(BigDecimal.ONE));
    }

    @Test
    void canPay() {
        final User user = new User(SampleData.TIERS.get(2), BigDecimal.ONE);
        assertTrue(user.canPay(BigDecimal.ONE));
    }

    @Test
    void cannotPay() {
        final User user = new User(SampleData.TIERS.get(2), BigDecimal.ONE);
        assertFalse(user.canPay(BigDecimal.TEN));
    }

    @Test
    void hasGame() {
        final Game game = new Game(4, "SKYRIM", BigDecimal.valueOf(87.88d));
        final User user = new User(SampleData.TIERS.get(1), BigDecimal.valueOf(123.15d));
        user.addGame(game);

        assertTrue(user.hasGame(game));
    }

    @Test
    void doesNotHaveGame() {
        final User user = new User(SampleData.TIERS.get(1), BigDecimal.valueOf(123));
        final Game alienGame = new Game(4, "SKYRIM", BigDecimal.valueOf(87.88d));

        assertFalse(user.hasGame(alienGame));
    }
}

