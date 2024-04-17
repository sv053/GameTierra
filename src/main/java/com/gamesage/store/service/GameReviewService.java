package com.gamesage.store.service;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.repository.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameReviewService {

    private final RatingRepository repository;

    public GameReviewService(RatingRepository repository) {
        this.repository = repository;
    }

    public Integer findRating(int gameId) {
        return repository.findRating(gameId);
    }

    public Integer findRatingsAmount(int gameId) {
        return repository.findRatingsAmount(gameId);
    }

    public List<Integer> createAll(List<Game> gamesToAdd) {
        return gamesToAdd.stream()
                .map(Game::getId)
                .map(this::createGame)
                .collect(Collectors.toList());
    }

    public Integer createGame(Integer gameIdToAdd) {
        return repository.addGame(gameIdToAdd);
    }

    public Integer addRating(Integer gameId, Boolean rating) {
        return repository.addRating(gameId, rating);
    }

    public Integer updateRating(Integer gameId, Boolean rating) {
        return repository.updateRating(gameId, rating);
    }
}

