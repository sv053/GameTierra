package com.gamesage.store.domain.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ResponseError {

    CVC_ERROR("Network refusal to perform the operation or incorrect CVV code"),
    INVALID_CARD_NUMBER("Incorrect card number"),
    FORMAT_ERROR("Please try again later"),
    EXPIRED_CARD("Expired card"),
    INSUFFICIENT_FUNDS("Insufficient funds");

    @JsonValue
    private final String cardErrorMessage;

    ResponseError(String cardErrorMessage) {
        this.cardErrorMessage = cardErrorMessage;
    }

    public String getCardErrorMessage() {
        return cardErrorMessage;
    }
}

