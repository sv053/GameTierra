package com.gamesage.service.domain.repository;

import com.gamesage.service.domain.model.Game;
import com.gamesage.service.domain.sample.SampleData;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameRepositoryTest {

    @Test
    void findByIdNotFoundWrongKey() {
        GameRepository repository = new GameRepository();
        repository.createAll(SampleData.GAMES);
        assertThrows(IllegalArgumentException.class, () -> repository.findById(1213313313));
    }

    @Test
    void createAll() {
        GameRepository repository = new GameRepository();
        repository.createAll(SampleData.GAMES);
        List<Game> games1 = Arrays.asList(
                new Game("GRAND", null)
        );
        repository.createAll(games1);
        assertTrue(repository.getGames().contains(games1.get(0)));
        assertTrue(repository.getGames().contains(new Game(1, "THE_WITCHER", null)));
    }
}

