package com.gamesage.store.domain.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CardError {

    CVC_ERROR("Отказ сети проводить операцию или неправильный CVV-код"),
    INVALID_CARD_NUMBER("Некорректный номер карты"),
    FORMAT_ERROR("Повторите попытку позже"),
    EXPIRED_CARD("Истёк срок действия карты"),
    INSUFFICIENT_FUNDS("Недостаточно средств");

    @JsonValue
    private final String cardErrorMessage;

    CardError(String cardErrorMessage) {
        this.cardErrorMessage = cardErrorMessage;
    }

    public String getCardErrorMessage() {
        return cardErrorMessage;
    }
}

