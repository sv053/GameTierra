package com.gamesage.store.service;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.sample.SampleData;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class StoreTest {
    private final Store store = Store.getInstance();

    @Test
    void buyGame() {
        BigDecimal initBalance = BigDecimal.valueOf(156.82);
        User user = new User(SampleData.TIERS.get(1), initBalance);
        int gameId = 1;
        Game game = new Game(gameId, "THE_WITCHER", BigDecimal.valueOf(17.28d));

        store.buyGame(gameId, user);

        BigDecimal cashback = game.getPrice().multiply(BigDecimal.valueOf
                (user.getTier().getCashbackPercentage() * 0.01));
        BigDecimal expectedBalance = initBalance.subtract(game.getPrice()).add(cashback)
                .setScale(2, RoundingMode.HALF_UP);

        assertEquals(expectedBalance, user.getBalance());
        assertTrue(user.getGames().contains(game));
    }

    @Test
    void gameDoesntExist() {
        assertThrows(IllegalArgumentException.class, () -> store.searchGame(1213313313));
    }

    @Test
    void didNotBuyAlreadyOwned() {
        int gameId = 4;
        Game game = Store.getInstance().searchGame(gameId);
        BigDecimal initBalance = BigDecimal.valueOf(156.82);
        User user = new User(SampleData.TIERS.get(1), initBalance);
        user.addGame(game);

        store.buyGame(gameId, user);

        assertEquals(initBalance, user.getBalance());
        assertTrue(user.getGames().contains(game));
    }

    @Test
    void calculateCashback() {
        User user = new User(SampleData.TIERS.get(1), BigDecimal.TEN);
        Game game = new Game(0, null, BigDecimal.valueOf(15));

        BigDecimal cashbackPercentage = BigDecimal.valueOf(user.getTier().getCashbackPercentage() * 0.01d);
        BigDecimal expectedCashback = game.getPrice().multiply(cashbackPercentage);

        assertEquals(expectedCashback, store.calculateCashback(game.getPrice(), user));
    }

    @Test
    void searchGameById() {
        int id = 7;
        Game gameToSearch = new Game(id, "ASSASSIN_S_CREED", null);

        assertEquals(gameToSearch, store.searchGame(id));
    }
}

