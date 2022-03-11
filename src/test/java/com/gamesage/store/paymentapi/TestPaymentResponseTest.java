package com.gamesage.store.paymentapi;

import com.gamesage.store.domain.model.Card;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class TestPaymentResponseTest {

    @Autowired
    TestPaymentResponse testPaymentResponse;

    @Test
    void formPaymentResponse_amountLessThanBalance() {
        Card card = new Card(
                1234567891234567L,
                "JOHN DOW",
                LocalDate.of(2023, 06, 06),
                123
        );
        BigDecimal amount = BigDecimal.valueOf(1001);
        PaymentRequest paymentRequest = new PaymentRequest(amount, card);
        PaymentResponse paymentResponse = testPaymentResponse.formPaymentResponse(paymentRequest);

        assertEquals("Недостаточно средств на карте", paymentResponse.getMessage());
    }

    @Test
    void formPaymentResponse_wrongCardNumber() {
        Card card = new Card(
                123456789L,
                "JOHN DOW",
                LocalDate.of(2023, 06, 06),
                123
        );
        BigDecimal amount = BigDecimal.TEN;
        PaymentRequest paymentRequest = new PaymentRequest(amount, card);
        PaymentResponse paymentResponse = testPaymentResponse.formPaymentResponse(paymentRequest);

        assertEquals("Проверьте правильность введенных данных карты или воспользуйтесь другой картой",
                paymentResponse.getMessage());
    }

    @Test
    void formPaymentResponse_wrongExpireDate() {
        Card card = new Card(
                1234567891234567L,
                "JOHN DOW",
                LocalDate.of(2020, 06, 06),
                123
        );
        BigDecimal amount = BigDecimal.TEN;
        PaymentRequest paymentRequest = new PaymentRequest(amount, card);
        PaymentResponse paymentResponse = testPaymentResponse.formPaymentResponse(paymentRequest);

        assertEquals("Свяжитесь с вашим банком или воспользуйтесь другой картой",
                paymentResponse.getMessage());
    }

    @Test
    void formPaymentResponse_wrongCvc() {
        Card card = new Card(
                1234567891234567L,
                "JOHN DOW",
                LocalDate.of(2027, 06, 06),
                12
        );
        BigDecimal amount = BigDecimal.TEN;
        PaymentRequest paymentRequest = new PaymentRequest(amount, card);
        PaymentResponse paymentResponse = testPaymentResponse.formPaymentResponse(paymentRequest);

        assertEquals("Проверьте правильность введенных данных карты или воспользуйтесь другой картой",
                paymentResponse.getMessage());
    }

    @Test
    void formPaymentResponse_wrongCardholderName() {
        Card card = new Card(
                1234567891234567L,
                "errJOHNDOW",
                LocalDate.of(2027, 06, 06),
                123
        );
        BigDecimal amount = BigDecimal.TEN;
        PaymentRequest paymentRequest = new PaymentRequest(amount, card);
        PaymentResponse paymentResponse = testPaymentResponse.formPaymentResponse(paymentRequest);

        assertEquals("Повторите попытку позже", paymentResponse.getMessage());
    }
}

