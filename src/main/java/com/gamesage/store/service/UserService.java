package com.gamesage.store.service;

import com.gamesage.store.domain.model.*;
import com.gamesage.store.domain.repository.UserUpdateRepository;
import com.gamesage.store.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    public boolean compareAmountWithLimit(BigDecimal amount, BigDecimal limit) {
        return Double.parseDouble(String.valueOf(limit.subtract(amount))) > 0.0d;
    }

    public boolean updateUserIfPaymentSucceed(PaymentResponse paymentResponse, int id) {
        if (paymentResponse.isSuccess()) {
            updateBalance(findById(id));
            return true;
        }
        return false;
    }

    public PaymentResponse formPaymentResponse(PaymentRequest paymentRequestIntent) {
        String transactionId = "bd6353c3-0ed6-4a65-946f-083664bf8dbd";
        PaymentResponse paymentResponse = new PaymentResponse(transactionId, true, "", 0);
        BigDecimal cardLimit = BigDecimal.valueOf(100);
        Card card = paymentRequestIntent.getCard();

        if (compareAmountWithLimit(cardLimit, paymentRequestIntent.getAmount())) {
            paymentResponse = new PaymentResponse(transactionId, false, "", 5054);
        }
        if (card.getCardholderName().startsWith("err")) {
            paymentResponse = new PaymentResponse(transactionId, false, "", 5030);
        }
        if (card.getExpireDate().isBefore(LocalDate.now())) {
            paymentResponse = new PaymentResponse(transactionId, false, "", 5033);
        }
        if (16 != card.getCardNumber().toString().length()) {
            paymentResponse = new PaymentResponse(transactionId, false, "", 5014);
        }
        Integer cvc = card.getCvcCode();
        if (cvc.toString().length() < 3 || 0 == cvc || null == cvc) {
            paymentResponse = new PaymentResponse(transactionId, false, "", 5006);
        }
        PaymentResponse finalPaymentResponse = paymentResponse;
        Optional<CardError> cardError = CardError.cardErrors.stream().filter(c -> c.getCode() == finalPaymentResponse.getResponseCode()).findFirst();
        if (cardError.isPresent()) {
            paymentResponse.setMessage(cardError.get().getCardErrorMessage());
        }
        return paymentResponse;
    }
}

