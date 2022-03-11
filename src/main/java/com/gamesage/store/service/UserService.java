package com.gamesage.store.service;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.UserUpdateRepository;
import com.gamesage.store.exception.EntityNotFoundException;
import com.gamesage.store.paymentapi.PaymentRequest;
import com.gamesage.store.paymentapi.PaymentResponse;
import com.gamesage.store.paymentapi.TestPaymentResponse;
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

    public User updateBalance(int userId, BigDecimal amount) {
        User user = findById(userId);
        user.depositBalance(amount);
        return repository.updateUserBalance(user);
    }

    public PaymentResponse updateUserIfPaymentSucceed(PaymentRequest paymentRequest, int id) {
        PaymentResponse paymentResponse = new TestPaymentResponse().formPaymentResponse(paymentRequest);
        if (paymentResponse.isSuccess()) {
            updateBalance(id, paymentRequest.getAmount());
        }
        return paymentResponse;
    }
}

