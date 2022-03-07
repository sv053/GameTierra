package com.gamesage.store.domain.model;

public class PaymentResponse {

    private final String transactionId;
    private final boolean success;
    private final String message;
    private final Integer errorCode;

    public PaymentResponse(String transactionId, boolean success, String message, Integer errorCode) {
        this.transactionId = transactionId;
        this.success = success;
        this.message = message;
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public boolean isSuccess() {
        return success;
    }
}

