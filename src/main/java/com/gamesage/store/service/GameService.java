package com.gamesage.store.service;


import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.GameRepository;
import com.gamesage.store.domain.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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

