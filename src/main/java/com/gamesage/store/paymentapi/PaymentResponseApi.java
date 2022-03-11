package com.gamesage.store.paymentapi;

import javax.validation.Valid;

public interface PaymentResponseApi {

    PaymentResponse formPaymentResponse(@Valid PaymentRequest paymentRequestIntent);
}
