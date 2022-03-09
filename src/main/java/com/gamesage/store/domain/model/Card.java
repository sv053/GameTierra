package com.gamesage.store.domain.model;

import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Validated
public class Card {

    @Pattern(regexp = "^[0-9]{16}")
    private final Long cardNumber;

    @Pattern(regexp = "^[a-zA-Z]{0,}")
    private final String cardholderName;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$")
    private final LocalDate expireDate;

    @Pattern(regexp = "^\\d{3}")
    private final Integer cvcCode;

    public Card(@Valid Long cardNumber, @Valid String cardholderName, @Valid LocalDate expireDate,
                @Valid Integer cvcCode) {
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

