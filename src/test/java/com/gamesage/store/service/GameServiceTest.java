package com.gamesage.store.service;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.repository.GameRepository;
import com.gamesage.store.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository repository;
    @InjectMocks
    private GameService gameService;

    @Test
    void findById_Success_TheRightGameIsFound() {
        int gameId = 1;
        Game game = new Game("fabula", BigDecimal.ONE);
        when(repository.findById(gameId)).thenReturn(Optional.of(game));

        assertEquals(game, gameService.findById(gameId));
    }

    @Test
    void findById_Fail_NotFound_Exception() {
        int gameId = 1;
        when(repository.findById(gameId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> gameService.findById(gameId));
    }
}

