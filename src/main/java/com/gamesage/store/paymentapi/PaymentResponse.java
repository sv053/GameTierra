package com.gamesage.store.paymentapi;

import com.gamesage.store.domain.model.ResponseError;

public class PaymentResponse {

    private final String transactionId;
    private final boolean success;
    private final ResponseError responseError;

    public PaymentResponse(String transactionId, boolean success, ResponseError responseError) {
        this.transactionId = transactionId;
        this.success = success;
        this.responseError = responseError;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public boolean isSuccess() {
        return success;
    }

    public ResponseError getResponseError() {
        return responseError;
    }
}

