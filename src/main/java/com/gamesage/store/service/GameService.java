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

    public GameService(@Qualifier("dbGameRepository") FindAllDependentRepository<Game, Integer> repository) {
        this.repository = repository;
    }

    public Game findById(int id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
    }

    public List<Game> findAll() {
        return repository.findAll();
    }

    public List<Game> findAllGamesByUserId(int ownerId) {
        return repository.findAllDependent(ownerId);
    }

    public List<Game> createAll(List<Game> gamesToAdd) {
        return repository.create(gamesToAdd);
    }

    public Game createOne(Game gameToAdd) {
        return repository.createOne(gameToAdd);
    }

}

