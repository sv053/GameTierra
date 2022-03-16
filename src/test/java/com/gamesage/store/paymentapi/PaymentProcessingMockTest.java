package com.gamesage.store.paymentapi;

import com.gamesage.store.domain.model.Card;
import com.gamesage.store.domain.model.ResponseError;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PaymentProcessingMockTest {

    private final PaymentProcessingMock paymentProcessingMock;

    PaymentProcessingMockTest() {
        this.paymentProcessingMock = new PaymentProcessingMock();
    }

    @Test
    void formPaymentResponse_amountLessThanBalance() {
        Card card = new Card(
                1234567891234567L,
                "JOHN DOW",
                LocalDate.of(2023, 6, 6),
                123
        );
        BigDecimal amount = BigDecimal.valueOf(1001);
        PaymentRequest paymentRequest = new PaymentRequest(amount, card);
        PaymentResponse paymentResponse = paymentProcessingMock.processPayment(paymentRequest);

        assertAll(
                () -> assertEquals(ResponseError.INSUFFICIENT_FUNDS, paymentResponse.getResponseError()),
                () -> assertTrue(paymentResponse.getTransactionId().length() > 0),
                () -> assertFalse(paymentResponse.isSuccess())
        );
    }

    @Test
    void formPaymentResponse_wrongCardNumber() {
        Card card = new Card(
                123456789L,
                "JOHN DOW",
                LocalDate.of(2023, 6, 6),
                123
        );
        BigDecimal amount = BigDecimal.TEN;
        PaymentRequest paymentRequest = new PaymentRequest(amount, card);
        PaymentResponse paymentResponse = paymentProcessingMock.processPayment(paymentRequest);

        assertAll(
                () -> assertEquals(ResponseError.INVALID_CARD_NUMBER, paymentResponse.getResponseError()),
                () -> assertTrue(paymentResponse.getTransactionId().length() > 0),
                () -> assertFalse(paymentResponse.isSuccess())
        );
    }

    @Test
    void formPaymentResponse_wrongExpireDate() {
        Card card = new Card(
                1234567891234567L,
                "JOHN DOW",
                LocalDate.of(2020, 6, 6),
                123
        );
        BigDecimal amount = BigDecimal.TEN;
        PaymentRequest paymentRequest = new PaymentRequest(amount, card);
        PaymentResponse paymentResponse = paymentProcessingMock.processPayment(paymentRequest);

        assertAll(
                () -> assertEquals(ResponseError.EXPIRED_CARD, paymentResponse.getResponseError()),
                () -> assertTrue(paymentResponse.getTransactionId().length() > 0),
                () -> assertFalse(paymentResponse.isSuccess())
        );
    }

    @Test
    void formPaymentResponse_wrongCvc() {
        Card card = new Card(
                1234567891234567L,
                "JOHN DOW",
                LocalDate.of(2027, 6, 6),
                12
        );
        BigDecimal amount = BigDecimal.TEN;
        PaymentRequest paymentRequest = new PaymentRequest(amount, card);
        PaymentResponse paymentResponse = paymentProcessingMock.processPayment(paymentRequest);

        assertAll(
                () -> assertEquals(ResponseError.CVC_ERROR, paymentResponse.getResponseError()),
                () -> assertTrue(paymentResponse.getTransactionId().length() > 0),
                () -> assertFalse(paymentResponse.isSuccess())
        );
    }

    @Test
    void formPaymentResponse_wrongCardholderName() {
        Card card = new Card(
                1234567891234567L,
                "errJOHNDOW",
                LocalDate.of(2027, 6, 6),
                123
        );
        BigDecimal amount = BigDecimal.TEN;
        PaymentRequest paymentRequest = new PaymentRequest(amount, card);
        PaymentResponse paymentResponse = paymentProcessingMock.processPayment(paymentRequest);

        assertEquals("Повторите попытку позже", paymentResponse.getResponseError().getCardErrorMessage());
        assertAll(
                () -> assertEquals(ResponseError.FORMAT_ERROR, paymentResponse.getResponseError()),
                () -> assertTrue(paymentResponse.getTransactionId().length() > 0),
                () -> assertFalse(paymentResponse.isSuccess())
        );
    }
}

