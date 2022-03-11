package com.gamesage.store.domain.model;

import javax.validation.constraints.Pattern;
import java.time.LocalDate;

public class Card {

    private final Long cardNumber;

    @Pattern(regexp = "^[A-Z]{1,19}\\s[A-Z]{1,19}$", message = "Please provide correct cardholder name")
    private final String cardholderName;

    private final LocalDate expireDate;

    private final Integer cvcCode;

    public Card(Long cardNumber, String cardholderName, LocalDate expireDate, Integer cvcCode) {
        this.cardNumber = cardNumber;
        this.cardholderName = cardholderName;
        this.expireDate = expireDate;
        this.cvcCode = cvcCode;
    }

    public Long getCardNumber() {
        return cardNumber;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public Integer getCvcCode() {
        return cvcCode;
    }
}

