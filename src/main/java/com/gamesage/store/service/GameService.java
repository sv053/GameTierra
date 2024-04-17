package com.gamesage.store.service;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.repository.FindAllDependentRepository;
import com.gamesage.store.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {

    private final FindAllDependentRepository<Game, Integer> repository;
    private final GameReviewService ratingService;

    public GameService(@Qualifier("dbGameRepository") FindAllDependentRepository<Game, Integer> repository, GameReviewService ratingService) {
        this.repository = repository;
        this.ratingService = ratingService;
    }

    public Game findById(int id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id, Game.class.getSimpleName()));
    }

    public List<Game> findAll() {
        return repository.findAll();
    }

    public List<Game> findAllGamesByUserId(int ownerId) {
        return repository.findAllDependent(ownerId);
    }

    public List<Game> createAll(List<Game> gamesToAdd) {
        List<Game> gamesWithIds = repository.create(gamesToAdd);
        gamesWithIds.stream()
                .map(Game::getId)
                .map(this::addGameToRatingTable)
                .forEach(ignore -> {
                });
        return gamesWithIds;
    }

    public Game createOne(Game gameToAdd) {
        Game game = repository.createOne(gameToAdd);
        addGameToRatingTable(game.getId());
        return game;
    }

    public Integer addGameToRatingTable(Integer gameIdToAdd) {
        return ratingService.createGame(gameIdToAdd);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}

