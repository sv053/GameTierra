package com.gamesage.store.paymentapi;

import com.gamesage.store.domain.model.CardError;

public class PaymentResponse {

    private final String transactionId;
    private final boolean success;
    private final CardError cardError;

    public PaymentResponse(String transactionId, boolean success, CardError cardError) {
        this.transactionId = transactionId;
        this.success = success;
        this.cardError = cardError;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public boolean isSuccess() {
        return success;
    }

    public CardError getCardError() {
        return cardError;
    }
}

