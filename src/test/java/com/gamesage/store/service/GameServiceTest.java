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

    @Test
    void buyGame_True() {
        GameRepository repository = mock(GameRepository.class);
        final int gameId = 1;

        Game game = new Game(gameId, "lago", BigDecimal.ONE);
        when(repository.findBy(gameId)).thenReturn(Optional.of(game));

        GameService gameService = new GameService(repository);

        User user = new User(null, new Tier("", 18), BigDecimal.TEN);

        assertTrue(gameService.buyGame(gameId, user));
    }

    @Test
    void buyGame_NewBalance() {
        GameRepository repository = mock(GameRepository.class);
        final int gameId = 1;

        Game game = new Game(gameId, "mar", BigDecimal.ONE);
        when(repository.findBy(gameId)).thenReturn(Optional.of(game));

        GameService gameService = new GameService(repository);

        BigDecimal initBalance = BigDecimal.valueOf(156.82);
        User user = new User("", new Tier("", 18), initBalance);

        gameService.buyGame(gameId, user);

        BigDecimal cashback = game.getPrice().multiply(BigDecimal.valueOf
                (user.getTier().getCashbackPercentage()));
        BigDecimal expectedBalance = initBalance.subtract(game.getPrice()).add(cashback)
                .setScale(2, RoundingMode.HALF_UP);

        assertEquals(expectedBalance, user.getBalance());
    }

    @Test
    void buyGame_PriceHigherThanBalance_SameBalance() {
        GameRepository repository = mock(GameRepository.class);
        final int gameId = 1;

        Game game = new Game(gameId, "addedGame", BigDecimal.TEN);
        when(repository.findBy(gameId)).thenReturn(Optional.of(game));

        GameService gameService = new GameService(repository);

        BigDecimal initBalance = BigDecimal.ONE;
        User user = new User("lark", new Tier("", 15), initBalance);

        gameService.buyGame(gameId, user);

        assertEquals(initBalance, user.getBalance());
    }

    @Test
    void buyGame_PriceHigherThanBalance_False() {
        GameRepository repository = mock(GameRepository.class);
        final int gameId = 1;

        Game game = new Game(gameId, "rio", BigDecimal.ONE);
        when(repository.findBy(gameId)).thenReturn(Optional.of(game));

        GameService gameService = new GameService(repository);

        User user = new User("", new Tier(null, .0), BigDecimal.ZERO);

        assertFalse(gameService.buyGame(game.getId(), user));
    }

    @Test
    void buyGame_AlreadyOwned_True() {
        GameRepository repository = mock(GameRepository.class);
        final int gameId = 1;

        Game game = new Game(gameId, "isla", BigDecimal.ONE);
        when(repository.findBy(gameId)).thenReturn(Optional.of(game));

        GameService gameService = new GameService(repository);

        User user = new User(null, null, BigDecimal.TEN);
        user.addGame(game);

        gameService.buyGame(game.getId(), user);

        assertTrue(user.getGames().contains(game));
    }

    @Test
    void findById_FoundGame() {
        GameRepository repository = mock(GameRepository.class);
        final int gameId = 1;
        Game game = new Game(gameId, "fabula", BigDecimal.ONE);
        when(repository.findBy(gameId)).thenReturn(Optional.of(game));

        GameService gameService = new GameService(repository);

        assertEquals(game, gameService.findById(1));
    }

    @Test
    void findById_GameDoesntExist_IAE() {
        GameRepository repository = mock(GameRepository.class);
        final int gameId = 1;
        when(repository.findBy(gameId)).thenReturn(Optional.empty());
        GameService gameService = new GameService(repository);

        assertThrows(IllegalArgumentException.class, () -> gameService.findById(gameId));
    }

    @Test
    void calculateCashback_CorrectCashback() {
        GameRepository repository = mock(GameRepository.class);
        final int gameId = 1;
        Game game = new Game(gameId, "fabula", BigDecimal.ONE);
        GameService gameService = new GameService(repository);

        User user = new User("marvel", new Tier("", 5.), BigDecimal.TEN);

        BigDecimal cashbackPercentage = BigDecimal.valueOf(user.getTier().getCashbackPercentage());
        BigDecimal expectedCashback = game.getPrice().multiply(cashbackPercentage);

        assertEquals(expectedCashback, gameService.calculateCashback(game.getPrice(), user));
    }
}

