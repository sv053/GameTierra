package com.gamesage.store.paymentapi;

import com.gamesage.store.domain.model.Card;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestPaymentResponse {

    @Test
    void formPaymentResponse_amountLessThanBalance() {
        PaymentMock paymentMock = new PaymentMock();
        paymentMock.setCARD_LIMIT(BigDecimal.ZERO);
        Card card = new Card(
                1234567891234567L,
                "JOHN DOW",
                LocalDate.of(2023, 06, 06),
                123
        );
        BigDecimal amount = BigDecimal.valueOf(1001);
        PaymentRequest paymentRequest = new PaymentRequest(amount, card);
        PaymentResponse paymentResponse = paymentMock.processPayment(paymentRequest);

        assertEquals("Недостаточно средств", paymentResponse.getResponseError().getCardErrorMessage());
    }

    @Test
    void formPaymentResponse_wrongCardNumber() {
        PaymentMock paymentMock = new PaymentMock();
        paymentMock.setCARD_LIMIT(BigDecimal.valueOf(100));
        Card card = new Card(
                123456789L,
                "JOHN DOW",
                LocalDate.of(2023, 06, 06),
                123
        );
        BigDecimal amount = BigDecimal.TEN;
        PaymentRequest paymentRequest = new PaymentRequest(amount, card);
        PaymentResponse paymentResponse = paymentMock.processPayment(paymentRequest);

        assertEquals("Некорректный номер карты", paymentResponse.getResponseError().getCardErrorMessage());
    }

    @Test
    void formPaymentResponse_wrongExpireDate() {
        PaymentMock paymentMock = new PaymentMock();
        paymentMock.setCARD_LIMIT(BigDecimal.valueOf(100));
        Card card = new Card(
                1234567891234567L,
                "JOHN DOW",
                LocalDate.of(2020, 06, 06),
                123
        );
        BigDecimal amount = BigDecimal.TEN;
        PaymentRequest paymentRequest = new PaymentRequest(amount, card);
        PaymentResponse paymentResponse = paymentMock.processPayment(paymentRequest);

        assertEquals("Истёк срок действия карты", paymentResponse.getResponseError().getCardErrorMessage());
    }

    @Test
    void formPaymentResponse_wrongCvc() {
        PaymentMock paymentMock = new PaymentMock();
        paymentMock.setCARD_LIMIT(BigDecimal.valueOf(100));
        Card card = new Card(
                1234567891234567L,
                "JOHN DOW",
                LocalDate.of(2027, 06, 06),
                12
        );
        BigDecimal amount = BigDecimal.TEN;
        PaymentRequest paymentRequest = new PaymentRequest(amount, card);
        PaymentResponse paymentResponse = paymentMock.processPayment(paymentRequest);

        assertEquals("Отказ сети проводить операцию или неправильный CVV-код",
                paymentResponse.getResponseError().getCardErrorMessage());
    }

    @Test
    void formPaymentResponse_wrongCardholderName() {
        PaymentMock paymentMock = new PaymentMock();
        paymentMock.setCARD_LIMIT(BigDecimal.valueOf(100));
        Card card = new Card(
                1234567891234567L,
                "errJOHNDOW",
                LocalDate.of(2027, 06, 06),
                123
        );
        BigDecimal amount = BigDecimal.TEN;
        PaymentRequest paymentRequest = new PaymentRequest(amount, card);
        PaymentResponse paymentResponse = paymentMock.processPayment(paymentRequest);

        assertEquals("Повторите попытку позже", paymentResponse.getResponseError().getCardErrorMessage());
    }
}

