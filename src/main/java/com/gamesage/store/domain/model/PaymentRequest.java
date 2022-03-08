package com.gamesage.store.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PaymentRequest {

    private final String publicId;
    private final BigDecimal amount;
    private final Card card;
//    private final String currency;
//    private final String email;
//    private final String accountId;
//    private final String invoiceId;
//    private final String description;
//    private final Boolean requireEmail;
//    private final Boolean retryPayment;
//    private final Data data;

    public PaymentRequest(String publicId, BigDecimal amount, Card card) {
        this.publicId = publicId;
        this.amount = amount;
        this.card = card;
    }

    public String getPublicId() {
        return publicId;
    }

    public Card getCard() {
        return card;
    }

    public BigDecimal getAmount() {
        return amount.setScale(2, RoundingMode.HALF_UP);
    }
}

