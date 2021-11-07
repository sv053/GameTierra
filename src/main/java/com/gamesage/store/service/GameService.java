package com.gamesage.store.service;


import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.Repository;

import java.math.BigDecimal;

public class GameService {
    private final Repository<Game> repository;

    public GameService(Repository<Game> rep) {
        repository = rep;
    }

    public Game searchGame(int id) {
        Game foundGame = repository.findBy(id);
        if (foundGame == null) throw new NullPointerException("Game not found");
        return foundGame;
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
            return true;
        }
        return false;
    }
}

