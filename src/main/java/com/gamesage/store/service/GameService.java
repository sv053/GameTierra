package com.gamesage.store.service;


import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.Repository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class GameService {

    private final Repository<Game, Integer> repository;

    public GameService(Repository<Game, Integer> repository) {
        this.repository = repository;
    }

    public Game findById(int id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Game with id %s not found", id)));
    }

    public List<Game> findAll(List<Game> games){
        if(!repository.getAll().contains(games.get(0))){
            repository.createAll(games);
        }
        return repository.getAll();
    }

    public BigDecimal calculateCashback(BigDecimal gamePrice, User user) {
        BigDecimal percentage = BigDecimal.valueOf(user.getTier().getCashbackPercentage());
        return gamePrice.multiply(percentage);
    }

    public boolean buyGame(int gameId, User user) {
        Game game = findById(gameId);
        BigDecimal price = game.getPrice();
        if (user.canPay(price) && (!user.hasGame(game))) {
            BigDecimal cashback = calculateCashback(price, user);
            user.withdrawBalance(price);
            user.depositBalance(cashback);
            user.addGame(game);
            return true;
        }
        return false;
    }
}

