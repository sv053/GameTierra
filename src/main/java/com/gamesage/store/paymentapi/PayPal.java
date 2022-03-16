package com.gamesage.store.paymentapi;

import com.gamesage.store.domain.model.Card;
import com.gamesage.store.domain.model.ResponseError;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class PayPal implements PaymentProcessingApi {

    private static final String TRANSACTION_ID = "cdy-5r3fiy-6ki6-6nbvh8g";
    private final BigDecimal cardLimit;
    private final BigDecimal zeroCardLimit;
    private PaymentResponse paymentResponse;

    public PayPal() {

        cardLimit = BigDecimal.valueOf(1000);
        zeroCardLimit = BigDecimal.ZERO;
        paymentResponse = new PaymentResponse(TRANSACTION_ID, true, null);
    }

    @Override
    public PaymentResponse processPayment(PaymentRequest paymentRequestIntent) {
        BigDecimal amount = paymentRequestIntent.getAmount();

        Card card = paymentRequestIntent.getCard();

        if (Double.parseDouble(String.valueOf(zeroCardLimit.compareTo(amount))) < 0.0d) {
            paymentResponse = new PaymentResponse(TRANSACTION_ID, false, ResponseError.INSUFFICIENT_FUNDS);
        }
        if (card.getCardholderName().startsWith("err")) {
            paymentResponse = new PaymentResponse(TRANSACTION_ID, false, ResponseError.FORMAT_ERROR);
        }
        if (card.getExpireDate().isBefore(LocalDate.now())) {
            paymentResponse = new PaymentResponse(TRANSACTION_ID, false, ResponseError.EXPIRED_CARD);
        }
        if (16 != card.getCardNumber().toString().length()) {
            paymentResponse = new PaymentResponse(TRANSACTION_ID, false, ResponseError.INVALID_CARD_NUMBER);
        }
        Integer cvc = card.getCvcCode();
        if (cvc.toString().length() < 3) {
            paymentResponse = new PaymentResponse(TRANSACTION_ID, false, ResponseError.CVC_ERROR);
        }
        return paymentResponse;
    }
}

