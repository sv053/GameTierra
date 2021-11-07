package com.gamesage.store.service;

import com.gamesage.store.domain.datasample.SampleData;
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
    }

    @Test
    void buyGameFalseSameBalance() {
        GameRepository repository = new GameRepository();
        repository.createAll(SampleData.GAMES);
        GameService gameService = new GameService(repository);

        BigDecimal initBalance = BigDecimal.valueOf(156.82);
        User user = new User(SampleData.TIERS.get(1), initBalance);

        gameService.buyGame(6, user);

        assertEquals(initBalance, user.getBalance());
    }

    @Test
    void buyGameFalseHasGame() {
        GameRepository repository = new GameRepository();
        repository.createAll(SampleData.GAMES);
        GameService gameService = new GameService(repository);
        BigDecimal initBalance = BigDecimal.valueOf(156.82);
        User user = new User(SampleData.TIERS.get(1), initBalance);
        int gameId = 1;
        Game game = repository.findBy(gameId);

        gameService.buyGame(1, user);

        assertTrue(user.getGames().contains(game));
    }

    @Test
    void buyGameTrue() {
        GameRepository repository = new GameRepository();
        repository.createAll(SampleData.GAMES);
        GameService gameService = new GameService(repository);

        BigDecimal initBalance = BigDecimal.valueOf(156.82);
        User user = new User(SampleData.TIERS.get(1), initBalance);

        assertTrue(gameService.buyGame(1, user));
    }

    @Test
    void buyGameFalsePriceHigherThanBalance() {
        GameRepository repository = new GameRepository();
        repository.createAll(SampleData.GAMES);
        GameService gameService = new GameService(repository);

        BigDecimal initBalance = BigDecimal.valueOf(156.82);
        User user = new User(SampleData.TIERS.get(1), initBalance);

        assertFalse(gameService.buyGame(6, user));
    }

    @Test
    void buyGameFalseAlreadyOwned() {
        GameRepository repository = new GameRepository();
        repository.createAll(SampleData.GAMES);
        GameService gameService = new GameService(repository);
        Integer gameId = 4;
        Game game = gameService.searchGame(gameId);

        BigDecimal initBalance = BigDecimal.valueOf(156.82);
        User user = new User(SampleData.TIERS.get(1), initBalance);
        user.addGame(game);

        gameService.buyGame(gameId, user);

        assertTrue(user.getGames().contains(game));
    }

    @Test
    void searchGameDoesntExist() {
        GameRepository repository = new GameRepository();
        repository.createAll(SampleData.GAMES);
        GameService gameService = new GameService(repository);
        assertThrows(NullPointerException.class, () -> gameService.searchGame(1213313));
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
    void searchGame() {
        GameRepository repository = new GameRepository();
        repository.createAll(SampleData.GAMES);
        GameService gameService = new GameService(repository);
        Integer id = 7;
        Game gameToSearch = new Game(id, "ASSASSIN_S_CREED", null);

        assertEquals(gameToSearch, gameService.searchGame(id));
    }
}

