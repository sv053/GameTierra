package com.gamesage.store.paymentapi;

import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
public interface PaymentProcessingApi {

    PaymentResponse processPayment(@Valid PaymentRequest paymentRequestIntent);
}
