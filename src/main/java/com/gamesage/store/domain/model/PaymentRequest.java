package com.gamesage.store.domain.model;

public class PaymentRequest {

    private final String publicId;
    private final Float amount;
    private final Card card;
//    private final String currency;
//    private final String email;
//    private final String accountId;
//    private final String invoiceId;
//    private final String description;
//    private final Boolean requireEmail;
//    private final Boolean retryPayment;
//    private final Data data;

    public PaymentRequest(String publicId, Float amount, Card card) {
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

    public Float getAmount() {
        return amount;
    }
}

