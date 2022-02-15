package com.gamesage.store.service;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.CreateManyRepository;
import com.gamesage.store.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class GameService {

    private final CreateManyRepository<Game, Integer> repository;

    public GameService(@Qualifier("dbGameRepository") CreateManyRepository<Game, Integer> repository) {
        this.repository = repository;
    }

    public Game findById(int id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
    }

    public List<Game> findAll() {
        return repository.findAll();
    }

    public List<Game> createAll(List<Game> gamesToAdd) {
        return repository.create(gamesToAdd);
    }

    public Game createOne(Game gameToAdd) {
        return repository.createOne(gameToAdd);
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

