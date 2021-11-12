package com.gamesage.store.service;


import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.Repository;

import java.math.BigDecimal;

public class GameService {
    private final Repository<Game> repository;

    public GameService(final Repository<Game> repository) {
        this.repository = repository;
    }

    public Game findById(final int id) {
        final Game foundGame = this.repository.findBy(id);
        if (foundGame == null) throw new IllegalArgumentException("Game with id " + id + " is not found");
        return foundGame;
    }

    public BigDecimal calculateCashback(final BigDecimal gamePrice, final User user) {
        final Double percentage = user.getTier().getCashbackPercentage();
        final BigDecimal percentageShare = BigDecimal.valueOf(percentage * 0.01d);
        return gamePrice.multiply(percentageShare);
    }

    public boolean buyGame(final int gameId, final User user) {
        final Game game = this.findById(gameId);
        final BigDecimal price = game.getPrice();
        if (user.canPay(price) && (!user.hasGame(game))) {
            final BigDecimal cashback = this.calculateCashback(price, user);
            user.withdrawBalance(price);
            user.depositBalance(cashback);
            user.addGame(game);
            return true;
        }
        return false;
    }
}

