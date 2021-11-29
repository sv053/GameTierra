package com.gamesage.store.controller;

import com.gamesage.store.domain.data.sample.SampleData;
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
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/findGame")
    public Game findGameById(@RequestParam Integer id) {
        gameService.findAll(SampleData.GAMES);
        Game game = gameService.findById(id);
        logger.info("Game is found: " + game);
        return game;
    }

    @GetMapping("/createGames")
    public List<Game> createGames(){
        List<Game> games = gameService.findAll(SampleData.GAMES);
        games.stream().forEach(u -> logger.info(u.toString()));
        return games;
    }
}

