package com.gamesage.store.paymentapi;

import com.gamesage.store.domain.model.Card;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class PaymentRequest {

    private final BigDecimal amount;

    private final Card card;

    public PaymentRequest(@DecimalMin("0.01") BigDecimal amount, Card card) {
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

