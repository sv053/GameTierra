package com.gamesage.store.controller;

import com.gamesage.store.domain.model.*;
import com.gamesage.store.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable Integer id) {
        return userService.findById(id);
    }

    @GetMapping
    public List<User> findAllUsers() {
        return userService.findAll();
    }

    @PostMapping
    public User createOne(@RequestBody User user) {
        return userService.createOne(user);
    }

    @PostMapping("/topup/{id}")
    public String tryTopUp(@PathVariable Integer id, @RequestBody PaymentRequest paymentRequest) {
        String result = "sth went wrong";

        if (paymentRequest.getAmount() < 0.01) {
            return "minimum payment is $0.01";
        }

        PaymentResponse paymentResponse = intentCloudpayment(paymentRequest);

        if (paymentResponse.isSuccess()) {
            userService.updateBalance(userService.findById(id));
            result = "success";
        } else {
            Optional<CardError> cardError = CardError.cardErrors.stream().filter(c -> c.getCode() == paymentResponse.getErrorCode()).findFirst();
            if (cardError.isPresent()) {
                result = cardError.get().getCardErrorMessage();
            }
        }
        return result;
    }

    //@PostMapping("https://api.cloudpayments.ru/")
    //public PaymentResponse intentCloudpayment(@RequestBody PaymentRequest paymentRequestIntent) {
    public PaymentResponse intentCloudpayment(PaymentRequest paymentRequestIntent) {
        float cardLimit = 100;
        float amountToPay = paymentRequestIntent.getAmount();
        Card card = paymentRequestIntent.getCard();

        PaymentResponse paymentResponse = new PaymentResponse("bd6353c3-0ed6-4a65-946f-083664bf8dbd", true, "", 0);

        if (amountToPay > cardLimit) {
            paymentResponse = new PaymentResponse("bd6353c3-0ed6-4a65-946f-083664bf8dbd", false, "", 5054);
        }
        if (card.getCardholderName().startsWith("err")) {
            paymentResponse = new PaymentResponse("bd6353c3-0ed6-4a65-946f-083664bf8dbd", false, "", 5030);
        }
        if (card.getExpireDate().isBefore(LocalDate.now())) {
            paymentResponse = new PaymentResponse("bd6353c3-0ed6-4a65-946f-083664bf8dbd", false, "", 5033);
        }
        if (16 != card.getCardNumber().toString().length()) {
            paymentResponse = new PaymentResponse("bd6353c3-0ed6-4a65-946f-083664bf8dbd", false, "", 5014);
        }
        Integer cvc = card.getCvcCode();
        if (cvc.toString().length() < 3 || 0 == cvc || null == cvc) {
            paymentResponse = new PaymentResponse("bd6353c3-0ed6-4a65-946f-083664bf8dbd", false, "", 5006);
        }
        return paymentResponse;
    }
}
// PaymentRequest example
//{
//        "publicId": "99ывоа8крииьодщ",
//        "amount": 10.0,
//        "card":{
//        "cardNumber": 4545984611112222,
//        "cardholderName": "errnumber",
//        "expireDate": "2025-12-12",
//        "cvcCode": 123
//        }
//        }

