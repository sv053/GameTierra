package com.gamesage.store.paymentapi;

import com.gamesage.store.domain.model.Card;
import com.gamesage.store.domain.model.CardError;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class PaymentMock implements PaymentProcessingApi {

    private static final String TRANSACTION_ID = "cdy-5r3fiy-6ki6-6nbvh8g";
    private BigDecimal CARD_LIMIT;

    public PaymentMock() {
        CARD_LIMIT = BigDecimal.valueOf(1000);
    }

    public void setCARD_LIMIT(BigDecimal CARD_LIMIT) {
        this.CARD_LIMIT = CARD_LIMIT;
    }

    @Override
    public PaymentResponse processPayment(PaymentRequest paymentRequestIntent) {
        PaymentResponse paymentResponse = new PaymentResponse(TRANSACTION_ID, true, null);
        BigDecimal amount = paymentRequestIntent.getAmount();

        Card card = paymentRequestIntent.getCard();

        if (Double.parseDouble(String.valueOf(CARD_LIMIT.subtract(amount))) < 0.0d) {
            paymentResponse = new PaymentResponse(TRANSACTION_ID, false, CardError.INSUFFICIENT_FUNDS);
        }
        if (card.getCardholderName().startsWith("err")) {
            paymentResponse = new PaymentResponse(TRANSACTION_ID, false, CardError.FORMAT_ERROR);
        }
        if (card.getExpireDate().isBefore(LocalDate.now())) {
            paymentResponse = new PaymentResponse(TRANSACTION_ID, false, CardError.EXPIRED_CARD);
        }
        if (16 != card.getCardNumber().toString().length()) {
            paymentResponse = new PaymentResponse(TRANSACTION_ID, false, CardError.INVALID_CARD_NUMBER);
        }
        Integer cvc = card.getCvcCode();
        if (cvc.toString().length() < 3) {
            paymentResponse = new PaymentResponse(TRANSACTION_ID, false, CardError.CVC_ERROR);
        }
        return paymentResponse;
    }
}

