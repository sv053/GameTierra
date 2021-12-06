package com.gamesage.store.service;


import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.CreateManyRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    private final CreateManyRepository<Game, Integer> repository;

    public GameService(CreateManyRepository<Game, Integer> repository) {
        this.repository = repository;
    }

//    public Optional<Game> findById(int id) {
//        return repository.findById(id);
//    }

    public List<Game> findAll() {
        return repository.findAll();
    }

    public List<Game> createAll(List<Game> gamesToAdd) {
        return repository.create(gamesToAdd);
    }

    public BigDecimal calculateCashback(BigDecimal gamePrice, User user) {
        BigDecimal percentage = BigDecimal.valueOf(user.getTier().getCashbackPercentage());
        return gamePrice.multiply(percentage);
    }

    public Game findById(int id){
        return repository.findById(id).orElseThrow(
                () -> new IllegalArgumentException(
                    String.format("Game with id %s not found", id)));
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

