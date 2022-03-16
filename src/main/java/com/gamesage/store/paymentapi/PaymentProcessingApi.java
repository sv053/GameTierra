package com.gamesage.store.paymentapi;

public interface PaymentProcessingApi {

    PaymentResponse processPayment(PaymentRequest paymentRequestIntent);
}
