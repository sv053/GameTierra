package com.gamesage.store.controller;

import com.gamesage.store.domain.data.sample.SampleData;
import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.repository.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/games")
public class GameController {

    private final GameRepository gameRepository;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public GameController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @PostMapping("/createGames")
    public List<Game> createGames(@RequestBody List<Game> gamesToSave){
        gameRepository.createAll(gamesToSave);
        gamesToSave.stream().forEach(u -> logger.info(u.toString()));
        return gamesToSave;
    }

    @PostMapping("/findGame")
    public Game findGameById(@RequestParam Integer id) {
        Optional<Game> game = gameRepository.findById(id);
        if(game.isPresent()){
            logger.info("Game is found: " + game.get());
        }else {
            logger.error("Game with id "+ id + " is found: " );
        }
        return game.get();
    }

    @GetMapping("/findAllGames")
    public List<Game> findGames(){
        List<Game> games = gameRepository.getAll();
        games.stream().forEach(u -> logger.info(u.toString()));
        return games;
    }
}

