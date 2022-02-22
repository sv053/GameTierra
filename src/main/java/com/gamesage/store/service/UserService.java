package com.gamesage.store.service;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.UpdateRepository;
import com.gamesage.store.domain.repository.db.DbGameRepository;
import com.gamesage.store.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UpdateRepository<User, Integer> repository;
    private final DbGameRepository gameRepository;

    public UserService(@Qualifier("dbUserRepository") UpdateRepository<User, Integer> repository,
                       DbGameRepository gameRepository) {
        this.repository = repository;
        this.gameRepository = gameRepository;
    }

    public User findById(int id) {
        User retrievedUser = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        List<Game> userGames = gameRepository.findGamesByUserId(id);
        retrievedUser.setGames(Set.copyOf(userGames));
        return retrievedUser;
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        for(User u : repository.findAll()){
            users.add(findById(u.getId()));
        }
        return users;
    }

    public User createOne(User userToAdd) {
        return repository.createOne(userToAdd);
    }

    public User updateBalance(User userToUpdate) {
        return repository.updateUserBalanceColumn(userToUpdate);
    }
}

