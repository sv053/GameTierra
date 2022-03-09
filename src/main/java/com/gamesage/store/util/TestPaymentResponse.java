package com.gamesage.store.util;

import com.gamesage.store.domain.model.Card;
import com.gamesage.store.domain.model.CardError;
import com.gamesage.store.domain.model.PaymentRequest;
import com.gamesage.store.domain.model.PaymentResponse;
import com.gamesage.store.service.UserService;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Validated
public class TestPaymentResponse {

    public static PaymentResponse formPaymentResponse(@Valid PaymentRequest paymentRequestIntent) {
        String transactionId = "bd6353c3-0ed6-4a65-946f-083664bf8dbd";
        PaymentResponse paymentResponse = new PaymentResponse(transactionId, true, "", 0);
        BigDecimal cardLimit = BigDecimal.valueOf(100);
        Card card = paymentRequestIntent.getCard();

        if (UserService.compareAmountWithLimit(cardLimit, paymentRequestIntent.getAmount())) {
            paymentResponse = new PaymentResponse(transactionId, false, "", 5054);
        }
        if (card.getCardholderName().startsWith("err")) {
            paymentResponse = new PaymentResponse(transactionId, false, "", 5030);
        }
        if (card.getExpireDate().isBefore(LocalDate.now())) {
            paymentResponse = new PaymentResponse(transactionId, false, "", 5033);
        }
//        if (16 != card.getCardNumber().toString().length()) {
//            paymentResponse = new PaymentResponse(transactionId, false, "", 5014);
//        }
//        Integer cvc = card.getCvcCode();
//        if (cvc.toString().length() < 3 || 0 == cvc || null == cvc) {
//            paymentResponse = new PaymentResponse(transactionId, false, "", 5006);
//        }
        PaymentResponse finalPaymentResponse = paymentResponse;
        Optional<CardError> cardError = CardError.cardErrors.stream().filter(c -> c.getCode() == finalPaymentResponse.getResponseCode()).findFirst();
        if (cardError.isPresent()) {
            paymentResponse.setMessage(cardError.get().getCardErrorMessage());
        }
        return paymentResponse;
    }
}

