package com.gamesage.store.paymentapi;

import com.gamesage.store.domain.model.Card;
import com.gamesage.store.domain.model.CardError;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

//@Validated
@Component
public class TestPaymentResponse implements PaymentResponseApi {

    @Override
    public PaymentResponse formPaymentResponse(PaymentRequest paymentRequestIntent) {
        String transactionId = "";
        PaymentResponse paymentResponse = new PaymentResponse(transactionId, true, "", 0);
        BigDecimal cardLimit = BigDecimal.valueOf(1000.0);
        BigDecimal amount = paymentRequestIntent.getAmount();

        Card card = paymentRequestIntent.getCard();

        int amountHigherCardlimit = 5054;
        int wrongCardholderName = 5030;
        int wrongCardNamber = 5014;
        int wrongExpireDate = 5033;
        int wrongCvc = 5006;

        if (Double.parseDouble(String.valueOf(cardLimit.subtract(amount))) < 0.0d) {
            paymentResponse = new PaymentResponse(transactionId, false, "", amountHigherCardlimit);
        }
        if (card.getCardholderName().startsWith("err")) {
            paymentResponse = new PaymentResponse(transactionId, false, "", wrongCardholderName);
        }
        if (card.getExpireDate().isBefore(LocalDate.now())) {
            paymentResponse = new PaymentResponse(transactionId, false, "", wrongExpireDate);
        }
        if (16 != card.getCardNumber().toString().length()) {
            paymentResponse = new PaymentResponse(transactionId, false, "", wrongCardNamber);
        }
        Integer cvc = card.getCvcCode();
        if (cvc.toString().length() < 3 || 0 == cvc || null == cvc) {
            paymentResponse = new PaymentResponse(transactionId, false, "", wrongCvc);
        }
        PaymentResponse finalPaymentResponse = paymentResponse;
        Optional<CardError> cardError = CardError.cardErrors.stream().filter(c -> c.getCode() == finalPaymentResponse.getResponseCode()).findFirst();
        if (cardError.isPresent()) {
            paymentResponse.setMessage(cardError.get().getCardErrorMessage());
        }
        return paymentResponse;
    }
}

