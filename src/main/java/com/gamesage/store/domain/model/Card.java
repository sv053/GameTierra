package com.gamesage.store.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Card {

    private final Long cardNumber16;
    private final String cardholderName;
    private final LocalDateTime expireDate;
    private final Integer cvcCode;
    private final BigDecimal amountToPay;

    public Card(Long cardNumber16, String cardholderName, LocalDateTime expireDate,
                Integer cvcCode, BigDecimal amountToPay) {
        this.cardNumber16 = cardNumber16;
        this.cardholderName = cardholderName;
        this.expireDate = expireDate;
        this.cvcCode = cvcCode;
        this.amountToPay = amountToPay;
    }
}

