package com.gamesage.store.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Atm {

    public static BigDecimal depositBalance(BigDecimal balance, BigDecimal amount) {
        balance = balance.add(amount).setScale(2, RoundingMode.HALF_UP);
        return balance;
    }

    public static BigDecimal withdrawBalance(BigDecimal balance, BigDecimal amount) {
        balance = balance.subtract(amount).setScale(2, RoundingMode.HALF_UP);
        return balance;
    }

    public static boolean canPay(BigDecimal balance, BigDecimal price) {
        return price.compareTo(balance) <= 0;
    }
}

