package com.gamesage.store.service;

import com.gamesage.store.domain.data.sample.SampleData;
import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.GameRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceIntegrationTest {

    @Test
    void buyGame_RightNewBalance() {
        final GameRepository repository = new GameRepository();
        final List<Game> games = Arrays.asList(new Game(null, "mistery island", BigDecimal.ONE));
        repository.createAll(games);
        final GameService gameService = new GameService(repository);

        final Game game = games.get(games.size() - 1);

        final BigDecimal initBalance = game.getPrice();
        final User user = new User(null, SampleData.TIERS.get(1), initBalance);

        gameService.buyGame(game.getId(), user);

        final BigDecimal cashback = game.getPrice().multiply(BigDecimal.valueOf
                (user.getTier().getCashbackPercentage()));
        final BigDecimal expectedBalance = initBalance.subtract(game.getPrice()).add(cashback)
                .setScale(2, RoundingMode.HALF_UP);

        assertEquals(expectedBalance, user.getBalance());
    }

    @Test
    void buyGame_PriceHigherThanBalance_SameBalance() {
        final GameRepository repository = new GameRepository();
        repository.createAll(SampleData.GAMES);
        final GameService gameService = new GameService(repository);

        final BigDecimal initBalance = BigDecimal.valueOf(156.82);
        final User user = new User("lark", SampleData.TIERS.get(1), initBalance);

        final Optional<Game> findGameResult = repository.getGames().stream()
                .filter(g -> g.getPrice().compareTo(initBalance) > 0).findFirst();
        if (findGameResult.isPresent()) {
            gameService.buyGame(findGameResult.get().getId(), user);
        }
        assertEquals(initBalance, user.getBalance());
    }

    @Test
    void buyGame_True() {
        final BigDecimal initBalance = BigDecimal.valueOf(156.82);
        final User user = new User("", SampleData.TIERS.get(1), initBalance);

        final GameRepository repository = new GameRepository();
        repository.createAll(SampleData.GAMES);
        final GameService gameService = new GameService(repository);
        final Optional<Game> game = repository.getGames().stream().findFirst();

        if (game.isPresent()) {
            assertTrue(gameService.buyGame(game.get().getId(), user));
        }
    }

    @Test
    void buyGame_AlreadyOwned_ContainsGame() {
        final GameRepository repository = new GameRepository();
        repository.createAll(SampleData.GAMES);
        final GameService gameService = new GameService(repository);
        final Integer gameId = repository.getGames().size() - 1;
        final Game game = repository.getGames().get(gameId);

        final BigDecimal initBalance = BigDecimal.valueOf(156.82);
        final User user = new User("", SampleData.TIERS.get(1), initBalance);
        user.addGame(game);

        gameService.buyGame(gameId, user);

        assertTrue(user.getGames().contains(game));
    }

    @Test
    void findGame_DoesntExist_IllegalArgumentException() {
        final GameRepository repository = new GameRepository();
        final GameService gameService = new GameService(repository);
        assertThrows(IllegalArgumentException.class, () -> gameService.findById(1213313));
    }

    @Test
    void calculateCashback_rightCashback() {
        final GameService gameService = new GameService(new GameRepository());

        final User user = new User("marvel", SampleData.TIERS.get(1), null);
        final Game game = new Game(null, null, BigDecimal.valueOf(15));

        final BigDecimal cashbackPercentage = BigDecimal.valueOf(user.getTier().getCashbackPercentage());
        final BigDecimal expectedCashback = game.getPrice().multiply(cashbackPercentage);

        assertEquals(expectedCashback, gameService.calculateCashback(game.getPrice(), user));
    }

    @Test
    void findGame_foundRightGame() {
        final GameRepository repository = new GameRepository();
        final List<Game> games = Arrays.asList(new Game(null, "mistery island", BigDecimal.ONE));
        repository.createAll(games);
        final GameService gameService = new GameService(repository);
        final Game gameToSearch = games.get(0);

        assertEquals(games.get(0), gameService.findById(gameToSearch.getId()));
    }
}

