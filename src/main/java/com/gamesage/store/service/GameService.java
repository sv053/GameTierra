package com.gamesage.store.service;


import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.Repository;

import java.math.BigDecimal;

public class GameService {

    private final Repository<Game, Integer> repository;

    public GameService(final Repository<Game, Integer> repository) {
        this.repository = repository;
    }

    public Game findById(final int id) {
        return this.repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(String.format("Game with id %s not found", id)));
    }

    public BigDecimal calculateCashback(final BigDecimal gamePrice, final User user) {
        BigDecimal percentage = BigDecimal.valueOf(user.getTier().getCashbackPercentage());
        return gamePrice.multiply(percentage);
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

