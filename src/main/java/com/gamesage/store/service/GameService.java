package com.gamesage.store.service;


import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.Repository;

import java.math.BigDecimal;
import java.util.Optional;

public class GameService {
    private final Repository<Game, Integer> repository;

    public GameService(Repository<Game, Integer> repository) {
        this.repository = repository;
    }

    public Game findById(int id) {
        Optional<Game> foundGame = this.repository.findBy(id);
        if (foundGame.isEmpty()) {
            throw new IllegalArgumentException("Game with id " + id + " is not found");
        }
        return foundGame.get();
    }

    public BigDecimal calculateCashback(BigDecimal gamePrice, User user) {
        final BigDecimal percentage = BigDecimal.valueOf(user.getTier().getCashbackPercentage());
        return gamePrice.multiply(percentage);
    }

    public boolean buyGame(int gameId, User user) {
        Game game = this.findById(gameId);
        BigDecimal price = game.getPrice();
        if (user.canPay(price) && (!user.hasGame(game))) {
            BigDecimal cashback = this.calculateCashback(price, user);
            user.withdrawBalance(price);
            user.depositBalance(cashback);
            user.addGame(game);
            return true;
        }
        return false;
    }
}

