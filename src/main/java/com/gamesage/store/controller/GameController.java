package com.gamesage.store.controller;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);


    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/{id}")
    public Game findGameById(@PathVariable Integer id) {
        return gameService.findById(id);
    }

    @GetMapping
    public List<Game> findAllGames() {
        var v = SecurityContextHolder.getContext();
        logger.error("credentials are " + v.getAuthentication().getCredentials().toString());
        return gameService.findAll();
    }

    @PostMapping
    public List<Game> createGames(@RequestBody List<Game> gamesToSave) {
        return gameService.createAll(gamesToSave);
    }
}

