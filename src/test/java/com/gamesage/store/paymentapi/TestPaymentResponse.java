package com.gamesage.store.paymentapi;

import com.gamesage.store.domain.model.Card;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TestPaymentResponse {

    @Autowired
    PaymentMock paymentMock;

    @Test
    void formPaymentResponse_amountLessThanBalance() {
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

        assertEquals("Недостаточно средств", paymentResponse.getCardError().getCardErrorMessage());
    }

    @Test
    void formPaymentResponse_wrongCardNumber() {
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

        assertEquals("Некорректный номер карты", paymentResponse.getCardError().getCardErrorMessage());
    }

    @Test
    void formPaymentResponse_wrongExpireDate() {
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

        assertEquals("Истёк срок действия карты", paymentResponse.getCardError().getCardErrorMessage());
    }

    @Test
    void formPaymentResponse_wrongCvc() {
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
                paymentResponse.getCardError().getCardErrorMessage());
    }

    @Test
    void formPaymentResponse_wrongCardholderName() {
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

        assertEquals("Повторите попытку позже", paymentResponse.getCardError().getCardErrorMessage());
    }
}

