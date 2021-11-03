package com.gamesage.store.service;

import com.gamesage.store.data.entity.User;
import com.gamesage.store.data.repository.SampleData;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AtmTest {

    @Test
    void withdrawBalance() {
        BigDecimal balance = BigDecimal.valueOf(123.15);
        BigDecimal amount = BigDecimal.ONE;
        User user = new User(SampleData.TIERS.get(4), balance);

        assertEquals(Atm.withdrawBalance(balance, amount), user.getBalance().subtract(amount));
    }

    @Test
    void depositBalance() {
        BigDecimal initialBalance = BigDecimal.valueOf(123.15);
        BigDecimal amount = BigDecimal.ONE;
        User user = new User(SampleData.TIERS.get(2), initialBalance);

        assertEquals(Atm.depositBalance(initialBalance, amount), user.getBalance().add(amount));
    }

    @Test
    void canPay() {
        assertTrue(Atm.canPay(BigDecimal.TEN, BigDecimal.ONE));
    }

    @Test
    void cannotPay() {
        assertFalse(Atm.canPay(BigDecimal.ONE, BigDecimal.TEN));
    }
}

