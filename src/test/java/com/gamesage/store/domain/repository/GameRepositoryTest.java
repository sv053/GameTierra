package com.gamesage.store.domain.repository;

import com.gamesage.store.domain.model.Game;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameRepositoryTest {

    @Test
    void findBy_NotFound() {
        GameRepository repository = new GameRepository();
        assertTrue(repository.findById(1213313313).isEmpty());
    }

    @Test
    void findBy_Success() {
        GameRepository repository = new GameRepository();
        List<Game> games = Arrays.asList(
                new Game("addedGame1", null),
                new Game("addedGame2", null)
        );
        repository.createAll(games);
        Game game = games.get(games.size() - 1);
        Optional<Game> foundGame = repository.findById(game.getId());

        assertTrue(foundGame.isPresent());
        if (foundGame.isPresent()) {
            assertEquals(game, foundGame.get());
        }
    }

    @Test
    void createAll() {
        GameRepository repository = new GameRepository();

        List<Game> games = Arrays.asList(
                new Game("addedGame1", null),
                new Game("addedGame2", null)
        );
        repository.createAll(games);

        assertEquals(games.size(), repository.getAll().size());
        assertTrue(repository.getAll().containsAll(games));
    }
}

