package com.gamesage.store.service;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.GameRepository;
import com.gamesage.store.domain.sample.SampleData;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    @Test
    void buyGame() {
        GameRepository repository = new GameRepository();
        repository.createAll(SampleData.GAMES);
        GameService gameService = new GameService(repository);
        BigDecimal initBalance = BigDecimal.valueOf(156.82);
        User user = new User(SampleData.TIERS.get(1), initBalance);
        int gameId = 1;
        Game game = new Game(gameId, "THE_WITCHER", BigDecimal.valueOf(17.28d));

        gameService.buyGame(gameId, user);

        BigDecimal cashback = game.getPrice().multiply(BigDecimal.valueOf
                (user.getTier().getCashbackPercentage() * 0.01));
        BigDecimal expectedBalance = initBalance.subtract(game.getPrice()).add(cashback)
                .setScale(2, RoundingMode.HALF_UP);

        assertEquals(expectedBalance, user.getBalance());
        assertTrue(user.getGames().contains(game));
    }

    @Test
    void gameDoesntExist() {
        GameRepository repository = new GameRepository();
        repository.createAll(SampleData.GAMES);
        GameService gameService = new GameService(repository);
        assertThrows(IllegalArgumentException.class, () -> gameService.searchGame(1213313313));
    }

    @Test
    void didNotBuyAlreadyOwned() {
        GameRepository repository = new GameRepository();
        repository.createAll(SampleData.GAMES);
        GameService gameService = new GameService(repository);
        int gameId = 4;
        Game game = gameService.searchGame(gameId);
        BigDecimal initBalance = BigDecimal.valueOf(156.82);
        User user = new User(SampleData.TIERS.get(1), initBalance);
        user.addGame(game);

        gameService.buyGame(gameId, user);

        assertEquals(initBalance, user.getBalance());
        assertTrue(user.getGames().contains(game));
    }

    @Test
    void calculateCashback() {
        GameRepository repository = new GameRepository();
        repository.createAll(SampleData.GAMES);
        GameService gameService = new GameService(repository);
        User user = new User(SampleData.TIERS.get(1), BigDecimal.TEN);
        Game game = new Game(0, null, BigDecimal.valueOf(15));

        BigDecimal cashbackPercentage = BigDecimal.valueOf(user.getTier().getCashbackPercentage() * 0.01d);
        BigDecimal expectedCashback = game.getPrice().multiply(cashbackPercentage);

        assertEquals(expectedCashback, gameService.calculateCashback(game.getPrice(), user));
    }

    @Test
    void searchGameById() {
        GameRepository repository = new GameRepository();
        repository.createAll(SampleData.GAMES);
        GameService gameService = new GameService(repository);
        int id = 7;
        Game gameToSearch = new Game(id, "ASSASSIN_S_CREED", null);

        assertEquals(gameToSearch, gameService.searchGame(id));
    }
}

