package com.gamesage.store.service;


import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.GameRepository;

import java.math.BigDecimal;

public class GameService {
    private final GameRepository repository;

    public GameService(GameRepository rep) {
        repository = rep;
    }

    public Game searchGame(int id) {
        return repository.findById(id);
    }

    public BigDecimal calculateCashback(BigDecimal gamePrice, User user) {
        BigDecimal percentageShare = BigDecimal.valueOf(user.getTier().getCashbackPercentage() * 0.01d);
        return gamePrice.multiply(percentageShare);
    }

    public boolean buyGame(int gameId, User user) {
        Game game = searchGame(gameId);
        BigDecimal price = game.getPrice();
        if (user.canPay(price) && (!user.hasGame(game))) {
            BigDecimal cashback = calculateCashback(price, user);
            user.withdrawBalance(price);
            user.depositBalance(cashback);
            user.addGame(game);
        }
        return false;
    }
}

