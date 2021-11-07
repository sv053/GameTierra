package com.gamesage.store.domain.repository;

import com.gamesage.store.domain.datasample.SampleData;
import com.gamesage.store.domain.model.Game;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameRepositoryTest {

    @Test
    void findByIdNotFound() {
        GameRepository repository = new GameRepository();
        repository.createAll(SampleData.GAMES);
        assertNull(repository.findBy(1213313313));
    }

    @Test
    void findByIdMapIsNull() {
        GameRepository repository = new GameRepository();
        assertNull(repository.findBy(1));
    }

    @Test
    void findByIdSuccess() {
        GameRepository repository = new GameRepository();
        repository.createAll(SampleData.GAMES);

        assertNotNull(repository.findBy(1));
    }

    @Test
    void createAll() {
        GameRepository repository = new GameRepository();
        repository.createAll(SampleData.GAMES);

        List<Game> games = Arrays.asList(
                new Game("addedGame1", null),
                new Game("addedGame2", null)
        );
        repository.createAll(games);

        int allGamesSize = SampleData.GAMES.size() + games.size();
        assertEquals(allGamesSize, repository.getGames().size());
    }
}

