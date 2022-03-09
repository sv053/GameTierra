package com.gamesage.store.service;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.PaymentResponse;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.UserUpdateRepository;
import com.gamesage.store.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {

    private final UserUpdateRepository repository;
    private final GameService gameService;

    public UserService(@Qualifier("dbUserRepository") UserUpdateRepository repository,
                       GameService gameService) {
        this.repository = repository;
        this.gameService = gameService;
    }

    public static boolean compareAmountWithLimit(BigDecimal amount, BigDecimal limit) {
        return Double.parseDouble(String.valueOf(limit.subtract(amount))) > 0.0d;
    }

    public User findById(int id) {
        User retrievedUser = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        List<Game> userGames = gameService.findAllGamesByUserId(retrievedUser.getId());
        retrievedUser.addGames(userGames);
        return retrievedUser;
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public User createOne(User userToAdd) {
        return repository.createOne(userToAdd);
    }

    public User updateBalance(User userToUpdate) {
        findById(userToUpdate.getId());
        return repository.updateUserBalance(userToUpdate);
    }

    public PaymentResponse updateUserIfPaymentSucceed(PaymentResponse paymentResponse, int id) {
        if (paymentResponse.isSuccess()) {
            updateBalance(findById(id));
        }
        return paymentResponse;
    }
}

