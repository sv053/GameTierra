package com.gamesage.store.paymentapi;

import com.gamesage.store.domain.model.Card;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PaymentRequest {

    private final BigDecimal amount;

    private final Card card;
//    private final String currency;
//    private final String email;
//    private final String accountId;
//    private final String invoiceId;
//    private final String description;
//    private final Boolean requireEmail;
//    private final Boolean retryPayment;

    public PaymentRequest(BigDecimal amount, Card card) {
        this.amount = amount;
        this.card = card;
    }

    public Card getCard() {
        return card;
    }

    public BigDecimal getAmount() {
        return amount.setScale(2, RoundingMode.HALF_UP);
    }
}

