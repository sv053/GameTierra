package com.gamesage.store.controller;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.service.GameService;
import com.gamesage.store.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;
    private final UserService userService;

    public GameController(GameService gameService, UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
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

    @GetMapping("/{game_id}/{user_id}")
    public String buyGame(@PathVariable Integer game_id, @PathVariable Integer user_id) {
        User user = userService.findById(user_id);
        int balanceUpdated = userService.updateBalance(user);
        boolean gameIsBought = gameService.buyGame(game_id, user);
        return gameIsBought && balanceUpdated == 1 ? "game is added" : "not added";
    }

}

