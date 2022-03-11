package com.gamesage.store.domain.model;

import java.util.List;

public class CardError {

    public static final List<CardError> cardErrors = List.of(
            new CardError(5006, "Error", "Отказ сети проводить операцию или неправильный CVV-код", "Проверьте правильность введенных данных карты или воспользуйтесь другой картой"),
            new CardError(5014, "Invalid Card Number", "Некорректный номер карты", "Проверьте правильность введенных данных карты или воспользуйтесь другой картой"),
            new CardError(5030, "Format Error", "Ошибка на стороне эквайера — неверно сформирована транзакция", "Повторите попытку позже"),
            new CardError(5033, "Expired Card Pickup", "Истёк срок утери карты", "Свяжитесь с вашим банком или воспользуйтесь другой картой"),
            new CardError(5054, "Insufficient Funds", "Недостаточно средств", "Недостаточно средств на карте")
    );
    private final Integer code;
    private final String name;
    private final String reason;
    private final String cardErrorMessage;

    public CardError(Integer code, String name, String reason, String cardErrorMessage) {
        this.code = code;
        this.name = name;
        this.reason = reason;
        this.cardErrorMessage = cardErrorMessage;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getReason() {
        return reason;
    }

    public String getCardErrorMessage() {
        return cardErrorMessage;
    }
}

