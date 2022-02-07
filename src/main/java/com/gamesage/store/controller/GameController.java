package com.gamesage.store.controller;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/{id}")
    public Game findGameById(@PathVariable Integer id) {
        Game foundGame = null;
        try {
            foundGame = gameService.findById(id);
        } catch (SQLException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_MODIFIED, "game not found", e);
        }
        return foundGame;
    }

    @GetMapping
    public List<Game> findAllGames() {
        return gameService.findAll();
    }

    @PostMapping
    public List<Game> createGames(@RequestBody List<Game> gamesToSave) {
        List<Game> games;
        try {
            games = gameService.createAll(gamesToSave);
        } catch (SQLException e) {
            throw new ResponseStatusException(
                    HttpStatus.PRECONDITION_FAILED, "game were not created", e);
        }
        return games;
    }
}

