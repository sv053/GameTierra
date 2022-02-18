package com.gamesage.store.controller;

import com.gamesage.store.service.StoreService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @PostMapping("/{game_id}/{user_id}")
    public boolean buyGame(@PathVariable Integer game_id, @PathVariable Integer user_id) {
        return storeService.buyGame(game_id, user_id);
    }
}

