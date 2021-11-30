package com.gamesage.store.controller;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/create")
    public List<Game> createGames(@RequestBody List<Game> gamesToSave){
        gameService.createAll(gamesToSave);
        GameController.logger.info(gamesToSave.size() + " games were added");
        return gameService.findAll();
    }

    @PostMapping("/read/{id}")
    public Game findGameById(@PathVariable Integer id) {
        Game game = gameService.findById(id);
        GameController.logger.info("Game with id "+ id + " : " + game);
        return game;
    }

    @GetMapping("/read")
    public List<Game> findGames(){
        List<Game> games = gameService.findAll();
        GameController.logger.info(games.size() + " games");
        return games;
    }
}

