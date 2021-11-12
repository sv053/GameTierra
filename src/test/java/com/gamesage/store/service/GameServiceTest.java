package com.gamesage.store.service;

import com.gamesage.store.domain.data.sample.SampleData;
import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.GameRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    @Test
    void buyGameTrueNewBalance() {
        final GameRepository repository = new GameRepository();
        repository.createAll(SampleData.GAMES);
        final GameService gameService = new GameService(repository);
        final BigDecimal initBalance = BigDecimal.valueOf(156.82);
        final User user = new User(SampleData.TIERS.get(1), initBalance);
        final int gameId = 1;
        final Game game = new Game(gameId, "THE_WITCHER", BigDecimal.valueOf(17.28d));

        gameService.buyGame(gameId, user);

        final BigDecimal cashback = game.getPrice().multiply(BigDecimal.valueOf
                (user.getTier().getCashbackPercentage() * 0.01));
        final BigDecimal expectedBalance = initBalance.subtract(game.getPrice()).add(cashback)
                .setScale(2, RoundingMode.HALF_UP);

        assertEquals(expectedBalance, user.getBalance());
    }

    @Test
    void buyGameFalseSameBalance() {
        final GameRepository repository = new GameRepository();
        repository.createAll(SampleData.GAMES);
        final GameService gameService = new GameService(repository);

        final BigDecimal initBalance = BigDecimal.valueOf(156.82);
        final User user = new User(SampleData.TIERS.get(1), initBalance);

        gameService.buyGame(6, user);

        assertEquals(initBalance, user.getBalance());
    }

    @Test
    void buyGameTrue() {
        final GameRepository repository = new GameRepository();
        repository.createAll(SampleData.GAMES);
        final GameService gameService = new GameService(repository);

        final BigDecimal initBalance = BigDecimal.valueOf(156.82);
        final User user = new User(SampleData.TIERS.get(1), initBalance);

        assertTrue(gameService.buyGame(1, user));
    }

    @Test
    void buyGameFalsePriceHigherThanBalance() {
        final GameRepository repository = new GameRepository();
        repository.createAll(SampleData.GAMES);
        final GameService gameService = new GameService(repository);
        final BigDecimal initBalance = BigDecimal.ONE;
        final Game game = repository.getGames().stream()
                .filter(
                        g -> g.getPrice().compareTo(initBalance) > 0)
                .findAny().get();
        final User user = new User(SampleData.TIERS.get(1), initBalance);

        assertFalse(gameService.buyGame(game.getId(), user));
    }

    @Test
    void buyGameFalseAlreadyOwned() {
        final GameRepository repository = new GameRepository();
        repository.createAll(SampleData.GAMES);
        final GameService gameService = new GameService(repository);
        final Integer gameId = 4;
        final Game game = repository.findBy(gameId);

        final BigDecimal initBalance = BigDecimal.valueOf(156.82);
        final User user = new User(SampleData.TIERS.get(1), initBalance);
        user.addGame(game);

        gameService.buyGame(gameId, user);

        assertTrue(user.getGames().contains(game));
    }

    @Test
    void searchGameDoesntExist() {
        final GameRepository repository = new GameRepository();
        repository.createAll(SampleData.GAMES);
        final GameService gameService = new GameService(repository);
        assertThrows(IllegalArgumentException.class, () -> gameService.findById(1213313));
    }

    @Test
    void calculateCashback() {
        final GameRepository repository = new GameRepository();
        repository.createAll(SampleData.GAMES);
        final GameService gameService = new GameService(repository);

        final User user = new User(SampleData.TIERS.get(1), null);
        final Game game = new Game(0, null, BigDecimal.valueOf(15));

        final BigDecimal cashbackPercentage = BigDecimal.valueOf(user.getTier().getCashbackPercentage() * 0.01d);
        final BigDecimal expectedCashback = game.getPrice().multiply(cashbackPercentage);

        assertEquals(expectedCashback, gameService.calculateCashback(game.getPrice(), user));
    }

    @Test
    void searchGame() {
        final GameRepository repository = new GameRepository();
        repository.createAll(SampleData.GAMES);
        final GameService gameService = new GameService(repository);
        final Integer gameId = 7;
        final Game gameToSearch = new Game(gameId, "ASSASSIN_S_CREED", null);

        assertEquals(gameToSearch, gameService.findById(gameId));
    }
}

