package com.gamesage.store.paymentapi;

import com.gamesage.store.domain.model.Card;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

public class PaymentRequest {

    @Min(1)
    private final BigDecimal amount;

    private final Card card;

    public PaymentRequest(BigDecimal amount, Card card) {
        this.amount = amount;
        this.card = card;
    }

    public Card getCard() {
        return card;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}

