package com.gamesage.store.controller;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.service.GameService;
import org.springframework.web.bind.annotation.*;

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
        return gameService.findById(id);
    }

    @GetMapping
    public List<Game> findAllGames() {
        return gameService.findAll();
    }

    @PostMapping
    public List<Game> createGames(@RequestBody List<Game> gamesToSave) {
        return gameService.createAll(gamesToSave);
    }
}

