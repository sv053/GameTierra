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

    @PostMapping("/")
    public List<Game> createGames(@RequestBody List<Game> gamesToSave){
        gameService.createAll(gamesToSave);
        return gameService.findAll();
    }

    @GetMapping("/{id}")
    public Game findGameById(@PathVariable Integer id) {
        Game game = gameService.findById(id);
        return game;
    }

    @GetMapping("/")
    public List<Game> findGames(){
        List<Game> games = gameService.findAll();
        return games;
    }
}

