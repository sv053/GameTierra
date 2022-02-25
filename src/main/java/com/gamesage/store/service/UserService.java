package com.gamesage.store.service;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.UpdateRepository;
import com.gamesage.store.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UpdateRepository repository;
    private final GameService gameService;

    public UserService(@Qualifier("dbUserRepository") UpdateRepository repository,
                       GameService gameService) {
        this.repository = repository;
        this.gameService = gameService;
    }

    public User findById(int id) {
        User retrievedUser = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        List<Game> userGames = gameService.findAllGamesByUserId(retrievedUser.getId());
        retrievedUser.setGames(Set.copyOf(userGames));
        return retrievedUser;
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public User createOne(User userToAdd) {
        return repository.createOne(userToAdd);
    }

    public User updateBalance(User userToUpdate) {
        return repository.updateUserBalance(userToUpdate);
    }
}

