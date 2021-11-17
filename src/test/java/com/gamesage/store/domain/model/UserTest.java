package com.gamesage.store.domain.model;

import com.gamesage.store.domain.data.sample.SampleData;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void addGame() {
        User user = new User(null, null, SampleData.TIERS.get(1), BigDecimal.valueOf(87.5));
        Game game = new Game(4, "SKYRIM", BigDecimal.valueOf(87.88d));

        assertTrue(user.addGame(game));
        assertTrue(user.getGames().contains(game));
    }

    @Test
    void withdrawBalance() {
        BigDecimal balance = BigDecimal.valueOf(123.15);
        User user = new User(null, null, SampleData.TIERS.get(4), balance);
        user.withdrawBalance(BigDecimal.ONE);
        assertEquals(user.withdrawBalance(BigDecimal.ONE), user.getBalance());
    }

    @Test
    void depositBalance() {
        BigDecimal initialBalance = BigDecimal.valueOf(123.15);
        User user = new User(null, null, SampleData.TIERS.get(2), initialBalance);
        user.depositBalance(BigDecimal.ONE);
        assertEquals(user.getBalance(), initialBalance.add(BigDecimal.ONE));
    }

    @Test
    void canPay() {
        User user = new User(null, null, SampleData.TIERS.get(2), BigDecimal.ONE);
        assertTrue(user.canPay(BigDecimal.ONE));
    }

    @Test
    void cannotPay() {
        User user = new User(null, null, SampleData.TIERS.get(2), BigDecimal.ONE);
        assertFalse(user.canPay(BigDecimal.TEN));
    }

    @Test
    void hasGame() {
        Game game = new Game(4, "SKYRIM", BigDecimal.valueOf(87.88d));
        User user = new User(null, null, SampleData.TIERS.get(1), BigDecimal.valueOf(123.15d));
        user.addGame(game);

        assertTrue(user.hasGame(game));
    }

    @Test
    void doesNotHaveGame() {
        User user = new User(null, null, SampleData.TIERS.get(1), BigDecimal.valueOf(123));
        Game alienGame = new Game(4, "SKYRIM", BigDecimal.valueOf(87.88d));

        assertFalse(user.hasGame(alienGame));
    }

    @Test
    void hashCode_sameIdsSameLogins_True() {
        User pedro = new User(8, "relampago", null, null);
        User pablo = new User(8, "relampago", null, null);
        assertEquals(pedro, pablo);
    }

    @Test
    void hashCode_differentIdsSameLogins_False() {
        User pedro = new User(7, "relampago", null, null);
        User pablo = new User(4, "relampago", null, null);
        assertNotEquals(pedro, pablo);
    }

    @Test
    void equals_sameIdsSameLogins_True() {
        User pedro = new User(3, "murcielago", null, null);
        User pablo = new User(3, "murcielago", null, null);
        assertEquals(pedro, pablo);
    }

    @Test
    void equals_differentIdsSameLogins_False() {
        User pedro = new User(7, "relampago", null, null);
        User pablo = new User(7, "murcielago", null, null);
        assertNotEquals(pedro, pablo);
    }

    @Test
    void equals_sameIdsDifferentLogins_True() {
        User pedro = new User(3, "murcielago", null, null);
        User pablo = new User(3, "relampago", null, null);
        assertNotEquals(pedro, pablo);
    }
}

