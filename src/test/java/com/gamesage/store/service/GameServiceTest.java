package com.gamesage.store.service;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.GameRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameServiceTest {

    private final GameRepository repository = mock(GameRepository.class);

    @Test
    void buyGame_Success_MethodReturnsTrue() {
        final int gameId = 1;
        Game game = new Game(gameId, "lago", BigDecimal.ONE);
        when(this.repository.findById(gameId)).thenReturn(Optional.of(game));

        GameService gameService = new GameService(this.repository);

        User user = new User(1, null, new Tier("", 18), BigDecimal.TEN);

        assertTrue(gameService.buyGame(gameId, user));
    }

    @Test
    void buyGame_Success_CheckingCorrectComputingOfTheNewBalance() {
        final int gameId = 1;
        Game game = new Game(gameId, "mar", BigDecimal.ONE);
        when(this.repository.findById(gameId)).thenReturn(Optional.of(game));

        GameService gameService = new GameService(this.repository);

        BigDecimal initBalance = BigDecimal.valueOf(156.82);
        User user = new User(1, "", new Tier("", 18), initBalance);

        gameService.buyGame(gameId, user);

        BigDecimal cashback = game.getPrice().multiply(BigDecimal.valueOf
                (user.getTier().getCashbackPercentage()));
        BigDecimal expectedBalance = initBalance.subtract(game.getPrice()).add(cashback)
                .setScale(2, RoundingMode.HALF_UP);

        assertEquals(expectedBalance, user.getBalance());
    }

    @Test
    void buyGame_Fail_WhenPriceIsHigherThanBalance_CheckIfTheBalanceIsTheSameAsBeforeTheIntent() {
        final int gameId = 1;
        Game game = new Game(gameId, "addedGame", BigDecimal.TEN);
        when(this.repository.findById(gameId)).thenReturn(Optional.of(game));

        GameService gameService = new GameService(this.repository);

        BigDecimal initBalance = BigDecimal.ONE;
        User user = new User(1, "lark", new Tier("", 15), initBalance);

        gameService.buyGame(gameId, user);

        assertEquals(initBalance, user.getBalance());
    }

    @Test
    void buyGame_Fail_WhenPriceHigherThanBalance_TheMethodReturnsFalse() {
        final int gameId = 1;
        Game game = new Game(gameId, "rio", BigDecimal.ONE);
        when(this.repository.findById(gameId)).thenReturn(Optional.of(game));

        GameService gameService = new GameService(this.repository);

        User user = new User(null, "", new Tier(null, .0), BigDecimal.ZERO);

        assertFalse(gameService.buyGame(gameId, user));
    }

    @Test
    void buyGame_Fail_CannotBuyAlreadyOwned_TheMethodReturnsFalse() {
        final int gameId = 1;
        Game game = new Game(gameId, "isla", BigDecimal.ONE);
        when(this.repository.findById(gameId)).thenReturn(Optional.of(game));

        GameService gameService = new GameService(this.repository);

        User user = new User(5, null, null, BigDecimal.TEN);
        user.addGame(game);

        assertFalse(gameService.buyGame(gameId, user));
    }

    @Test
    void findById_Success_TheRightGameIsFound() {
        final int gameId = 1;
        Game game = new Game(gameId, "fabula", BigDecimal.ONE);
        when(this.repository.findById(gameId)).thenReturn(Optional.of(game));

        GameService gameService = new GameService(this.repository);

        assertEquals(game, gameService.findById(gameId));
    }

    @Test
    void findById_Fail_TheGameIsNotFound_Exception() {
        final int gameId = 1;
        when(this.repository.findById(gameId)).thenReturn(Optional.empty());
        GameService gameService = new GameService(this.repository);

        assertThrows(IllegalArgumentException.class, () -> gameService.findById(gameId));
        try {
            gameService.findById(gameId);
        } catch (final IllegalArgumentException e) {
            assertEquals("Game with id " + gameId + " not found",
                    e.getMessage());
            return;
        }
        fail("Expected validation exception was not thrown");
    }

    @Test
    void calculateCashback_Success_CheckCorrectComputingOfTheCashback() {
        GameRepository repository = mock(GameRepository.class);
        Game game = new Game(1, "fabula", BigDecimal.ONE);
        GameService gameService = new GameService(repository);

        User user = new User(7, "marvel", new Tier("", 5.), BigDecimal.TEN);

        BigDecimal cashbackPercentage = BigDecimal.valueOf(user.getTier().getCashbackPercentage());
        BigDecimal expectedCashback = game.getPrice().multiply(cashbackPercentage);

        assertEquals(expectedCashback, gameService.calculateCashback(game.getPrice(), user));
    }
}

