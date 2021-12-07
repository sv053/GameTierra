package com.gamesage.store.service;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.GameRepository;
import com.gamesage.store.exception.EntryNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository repository;
    @InjectMocks
    private GameService gameService;

    @Test
    void buyGame_Success_ReturnsTrue() {
        int gameId = 1;
        Game game = new Game(gameId, "lago", BigDecimal.ONE);
        when(repository.findById(gameId)).thenReturn(Optional.of(game));

        User user = new User(1, null, new Tier("", 18), BigDecimal.TEN);

        assertTrue(gameService.buyGame(gameId, user));
    }

    @Test
    void buyGame_Success_BalanceUpdated() {
        int gameId = 1;
        Game game = new Game(gameId, "mar", BigDecimal.ONE);
        when(repository.findById(gameId)).thenReturn(Optional.of(game));

        BigDecimal initBalance = BigDecimal.valueOf(156.82);
        User user = new User(1, "", new Tier("", 18), initBalance);

        gameService.buyGame(gameId, user);

        BigDecimal cashback = game.getPrice()
                .multiply(BigDecimal.valueOf(user.getTier().getCashbackPercentage()));
        BigDecimal expectedBalance = initBalance
                .subtract(game.getPrice())
                .add(cashback)
                .setScale(2, RoundingMode.HALF_UP);

        assertEquals(expectedBalance, user.getBalance());
    }

    @Test
    void buyGame_Fail_WhenPriceIsHigherThanBalance_BalanceUnchanged() {
        int gameId = 1;
        Game game = new Game(gameId, "addedGame", BigDecimal.TEN);
        when(repository.findById(gameId)).thenReturn(Optional.of(game));

        BigDecimal initBalance = BigDecimal.ONE;
        User user = new User(1, "lark", new Tier("", 15), initBalance);

        gameService.buyGame(gameId, user);

        assertEquals(initBalance, user.getBalance());
    }

    @Test
    void buyGame_Fail_WhenPriceHigherThanBalance_ReturnsFalse() {
        int gameId = 1;
        Game game = new Game(gameId, "rio", BigDecimal.ONE);
        when(repository.findById(gameId)).thenReturn(Optional.of(game));

        User user = new User(null, "", new Tier(null, .0), BigDecimal.ZERO);

        assertFalse(gameService.buyGame(gameId, user));
    }

    @Test
    void buyGame_Fail_CannotBuyAlreadyOwned_ReturnsFalse() {
        int gameId = 1;
        Game game = new Game(gameId, "isla", BigDecimal.ONE);
        when(repository.findById(gameId)).thenReturn(Optional.of(game));

        User user = new User(5, null, null, BigDecimal.TEN);
        user.addGame(game);

        assertFalse(gameService.buyGame(gameId, user));
    }

    @Test
    void findById_Success_TheRightGameIsFound() {
        int gameId = 1;
        Game game = new Game(gameId, "fabula", BigDecimal.ONE);
        when(repository.findById(gameId)).thenReturn(Optional.of(game));

        assertEquals(game, gameService.findById(gameId));
    }

    @Test
    void findGame_Fail_NotFound_Exception() {
        int gameId = 1;
        when(repository.findById(gameId)).thenReturn(Optional.empty());

        EntryNotFoundException e = assertThrows(
                EntryNotFoundException.class, () -> gameService.findById(gameId));
        assertEquals("Entry with id 1 not found", e.getMessage());

    }

    @Test
    void calculateCashback_Success_CheckCashback() {
        Game game = new Game(1, "fabula", BigDecimal.ONE);

        User user = new User(7, "marvel", new Tier("", 5.), BigDecimal.TEN);

        BigDecimal cashbackPercentage = BigDecimal.valueOf(user.getTier().getCashbackPercentage());
        BigDecimal expectedCashback = game.getPrice().multiply(cashbackPercentage);

        assertEquals(expectedCashback, gameService.calculateCashback(game.getPrice(), user));
    }
}

