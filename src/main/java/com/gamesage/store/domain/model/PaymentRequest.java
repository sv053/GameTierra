package com.gamesage.store.domain.model;

import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Validated
public class PaymentRequest {

    @NonNull
    @Min(1)
    private final BigDecimal amount;

    @NonNull
    private final Card card;
//    private final String currency;
//    private final String email;
//    private final String accountId;
//    private final String invoiceId;
//    private final String description;
//    private final Boolean requireEmail;
//    private final Boolean retryPayment;

    public PaymentRequest(@Valid BigDecimal amount, @Valid Card card) {
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

