package com.gamesage.store.service;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.UserUpdateRepository;
import com.gamesage.store.exception.EntityNotFoundException;
import com.gamesage.store.paymentapi.PaymentProcessingApi;
import com.gamesage.store.paymentapi.PaymentRequest;
import com.gamesage.store.paymentapi.PaymentResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {

    private final UserUpdateRepository repository;
    private final GameService gameService;
    private final PaymentProcessingApi paymentProcessingApi;

    public UserService(@Qualifier("dbUserRepository") UserUpdateRepository repository,
                       GameService gameService, PaymentProcessingApi paymentProcessingApi) {
        this.repository = repository;
        this.gameService = gameService;
        this.paymentProcessingApi = paymentProcessingApi;
    }

    public User findById(int id) {
        User retrievedUser = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        List<Game> userGames = gameService.findAllGamesByUserId(retrievedUser.getId());
        retrievedUser.addGames(userGames);
        return retrievedUser;
    }

    public User findByLogin(String login) {
        User retrievedUser = repository.findByLogin(login).orElseThrow(() -> new EntityNotFoundException(login));
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
        return repository.updateUserBalance(userToUpdate);
    }

    private User depositAndUpdateBalance(User user, BigDecimal amountToAdd) {
        user.depositBalance(amountToAdd);
        return updateBalance(user);
    }

    public PaymentResponse topUpBalance(PaymentRequest paymentRequest, int id) {
        User user = findById(id);
        PaymentResponse paymentResponse = paymentProcessingApi.processPayment(paymentRequest);
        if (paymentResponse.isSuccess()) {
            depositAndUpdateBalance(user, paymentRequest.getAmount());
        }
        return paymentResponse;
    }
}

