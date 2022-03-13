package com.gamesage.store.service;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.UserUpdateRepository;
import com.gamesage.store.exception.EntityNotFoundException;
import com.gamesage.store.paymentapi.PaymentMock;
import com.gamesage.store.paymentapi.PaymentRequest;
import com.gamesage.store.paymentapi.PaymentResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {

    private final UserUpdateRepository repository;
    private final GameService gameService;
    private final PaymentMock paymentMock;

    public UserService(@Qualifier("dbUserRepository") UserUpdateRepository repository,
                       GameService gameService, PaymentMock paymentMock) {
        this.repository = repository;
        this.gameService = gameService;
        this.paymentMock = paymentMock;
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
        //   findById(userToUpdate.getId());
        return repository.updateUserBalance(userToUpdate);
    }

    private User updateBalance(User user, BigDecimal amount) {
        user.depositBalance(amount);
        return updateBalance(user);
    }

    @Transactional
    public PaymentResponse updateUserIfPaymentSucceed(PaymentRequest paymentRequest, int id) {
        User user = findById(id);
        PaymentResponse paymentResponse = paymentMock.processPayment(paymentRequest);
        if (paymentResponse.isSuccess()) {
            updateBalance(user, paymentRequest.getAmount());
        }
        return paymentResponse;
    }
}

