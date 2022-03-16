package com.gamesage.store.paymentapi;

import com.gamesage.store.domain.model.Card;
import com.gamesage.store.domain.model.ResponseError;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TestPayPal {

    @Test
    void formPaymentResponse_amountLessThanBalance() {
        PayPal payPal = new PayPal();
        Card card = new Card(
                1234567891234567L,
                "JOHN DOW",
                LocalDate.of(2023, 06, 06),
                123
        );
        BigDecimal amount = BigDecimal.valueOf(1001);
        PaymentRequest paymentRequest = new PaymentRequest(amount, card);
        PaymentResponse paymentResponse = payPal.processPayment(paymentRequest);

        assertAll(
                () -> assertEquals(ResponseError.INSUFFICIENT_FUNDS.getCardErrorMessage(), paymentResponse.getResponseError().getCardErrorMessage()),
                () -> assertTrue(paymentResponse.getTransactionId().length() > 0),
                () -> assertFalse(paymentResponse.isSuccess())
        );
    }

    @Test
    void formPaymentResponse_wrongCardNumber() {
        PayPal payPal = new PayPal();
        Card card = new Card(
                123456789L,
                "JOHN DOW",
                LocalDate.of(2023, 06, 06),
                123
        );
        BigDecimal amount = BigDecimal.TEN;
        PaymentRequest paymentRequest = new PaymentRequest(amount, card);
        PaymentResponse paymentResponse = payPal.processPayment(paymentRequest);

        assertAll(
                () -> assertEquals(ResponseError.INVALID_CARD_NUMBER.getCardErrorMessage(), paymentResponse.getResponseError().getCardErrorMessage()),
                () -> assertTrue(paymentResponse.getTransactionId().length() > 0),
                () -> assertFalse(paymentResponse.isSuccess())
        );
    }

    @Test
    void formPaymentResponse_wrongExpireDate() {
        PayPal payPal = new PayPal();
        Card card = new Card(
                1234567891234567L,
                "JOHN DOW",
                LocalDate.of(2020, 06, 06),
                123
        );
        BigDecimal amount = BigDecimal.TEN;
        PaymentRequest paymentRequest = new PaymentRequest(amount, card);
        PaymentResponse paymentResponse = payPal.processPayment(paymentRequest);

        assertAll(
                () -> assertEquals(ResponseError.EXPIRED_CARD.getCardErrorMessage(), paymentResponse.getResponseError().getCardErrorMessage()),
                () -> assertTrue(paymentResponse.getTransactionId().length() > 0),
                () -> assertFalse(paymentResponse.isSuccess())
        );
    }

    @Test
    void formPaymentResponse_wrongCvc() {
        PayPal payPal = new PayPal();
        Card card = new Card(
                1234567891234567L,
                "JOHN DOW",
                LocalDate.of(2027, 06, 06),
                12
        );
        BigDecimal amount = BigDecimal.TEN;
        PaymentRequest paymentRequest = new PaymentRequest(amount, card);
        PaymentResponse paymentResponse = payPal.processPayment(paymentRequest);

        assertAll(
                () -> assertEquals(ResponseError.CVC_ERROR.getCardErrorMessage(), paymentResponse.getResponseError().getCardErrorMessage()),
                () -> assertTrue(paymentResponse.getTransactionId().length() > 0),
                () -> assertFalse(paymentResponse.isSuccess())
        );
    }

    @Test
    void formPaymentResponse_wrongCardholderName() {
        PayPal payPal = new PayPal();
        Card card = new Card(
                1234567891234567L,
                "errJOHNDOW",
                LocalDate.of(2027, 06, 06),
                123
        );
        BigDecimal amount = BigDecimal.TEN;
        PaymentRequest paymentRequest = new PaymentRequest(amount, card);
        PaymentResponse paymentResponse = payPal.processPayment(paymentRequest);

        assertEquals("Повторите попытку позже", paymentResponse.getResponseError().getCardErrorMessage());
        assertAll(
                () -> assertEquals(ResponseError.FORMAT_ERROR.getCardErrorMessage(), paymentResponse.getResponseError().getCardErrorMessage()),
                () -> assertTrue(paymentResponse.getTransactionId().length() > 0),
                () -> assertFalse(paymentResponse.isSuccess())
        );
    }
}

