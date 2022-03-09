package com.gamesage.store.domain.model;

public class PaymentResponse {

    private final String transactionId;
    private final boolean success;
    private final Integer responseCode;
    private String message;

    public PaymentResponse(String transactionId, boolean success, String message, Integer errorCode) {
        this.transactionId = transactionId;
        this.success = success;
        this.message = message;
        this.responseCode = errorCode;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public boolean isSuccess() {
        return success;
    }
}

