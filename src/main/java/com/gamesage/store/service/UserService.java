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
    private final PaymentProcessingApi payPal;

    public UserService(@Qualifier("dbUserRepository") UserUpdateRepository repository,
                       GameService gameService, PaymentProcessingApi payPal) {
        this.repository = repository;
        this.gameService = gameService;
        this.payPal = payPal;
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
        return repository.updateUserBalance(userToUpdate);
    }

    private User updateBalance(User user, BigDecimal amount) {
        user.depositBalance(amount);
        return updateBalance(user);
    }

    public PaymentResponse updateUserIfPaymentSucceed(PaymentRequest paymentRequest, int id) {
        User user = findById(id);
        PaymentResponse paymentResponse = payPal.processPayment(paymentRequest);
        if (paymentResponse.isSuccess()) {
            updateBalance(user, paymentRequest.getAmount());
        }
        return paymentResponse;
    }
}

