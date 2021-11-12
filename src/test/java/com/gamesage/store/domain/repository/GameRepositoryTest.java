package com.gamesage.store.domain.repository;

import com.gamesage.store.domain.data.sample.SampleData;
import com.gamesage.store.domain.model.Game;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GameRepositoryTest {

    @Test
    void findByIdNotFound() {
        final GameRepository repository = new GameRepository();
        repository.createAll(SampleData.GAMES);
        assertNull(repository.findBy(1213313313));
    }

    @Test
    void findByIdSuccess() {
        final GameRepository repository = new GameRepository();
        repository.createAll(SampleData.GAMES);
        final Game game = repository.getGames().stream().findFirst().get();
        assertEquals(game, repository.findBy(game.getId()));
    }

    @Test
    void createAll() {
        final GameRepository repository = new GameRepository();

        final List<Game> games = Arrays.asList(
                new Game("addedGame1", null),
                new Game("addedGame2", null)
        );
        repository.createAll(games);

        assertEquals(games.size(), repository.getGames().size());
    }
}

