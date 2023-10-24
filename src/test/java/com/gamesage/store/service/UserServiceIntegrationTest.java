package com.gamesage.store.service;

import com.gamesage.store.domain.model.Card;
import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.exception.EntityNotFoundException;
import com.gamesage.store.paymentapi.PaymentRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource("classpath:application-test.properties")
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    void findById_Success() {
        User user = new User(null, "aquamarina", "delmar", new Tier(
                3, null, 10.d), BigDecimal.TEN);
        User userToFind = userService.createOne(user);
        User foundUser = userService.findById(userToFind.getId());

        assertEquals(userToFind, foundUser);
    }

    @Test
    void findById_Failure() {
        assertThrows(EntityNotFoundException.class, () -> userService.findById(77777));
    }

    @Test
    void findAll_Success() {
        List<User> usersToAdd = new ArrayList<>();
        usersToAdd.add(userService.createOne(new User(null, "primero", "key1", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN)));
        usersToAdd.add(userService.createOne(new User(null, "segundo", "key2", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN)));
        usersToAdd.add(userService.createOne(new User(null, "tercero", "key3", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN)));
        List<User> users = userService.findAll();

        assertTrue(users.containsAll(usersToAdd));
    }

    @Test
    void createUser_Success() {
        User user = new User(null, "loco", "lerida", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);
        User addedUser = userService.createOne(user);
        List<User> users = userService.findAll();
        assertAll(
                () -> assertTrue(users.contains(addedUser)),
                () -> assertNotNull(addedUser.getId()));
    }

    @Test
    void updateUserBalance_Success() {
        BigDecimal balance = BigDecimal.TEN;
        User user = userService.createOne(new User(null, "luara", "cuarto", new Tier(
                3, "SILVER", 10.d), balance));
        assertEquals(balance, user.getBalance());

        BigDecimal amount = BigDecimal.ONE;
        BigDecimal newBalance = balance.add(amount).setScale(2, RoundingMode.HALF_UP);
        user.depositBalance(amount);
        user = userService.updateBalance(user);

        assertEquals(newBalance, user.getBalance());
    }

    @Test
    void topUpBalance_Failure_WrongUserId() {
        int userId = 1;
        BigDecimal amount = BigDecimal.TEN;
        Card card = new Card(1111_2222_3333_4444L,
                "TA IA",
                LocalDate.of(LocalDate.now().getYear() + 1, 1, 1),
                888);
        PaymentRequest paymentRequest = new PaymentRequest(amount, card);
        assertThrows(EntityNotFoundException.class, () -> userService.topUpBalance(paymentRequest, userId));
    }

    @Test
    void topUpBalance_Success_RightUserId() {
        BigDecimal balance = BigDecimal.valueOf(10.d);
        User user = userService.createOne(new User(null, "lsla", "canaria", new Tier(
                3, "SILVER", 10.d), balance));
        BigDecimal amount = BigDecimal.valueOf(10.d);

        Card card = new Card(1111_2222_3333_4444L,
                "TA IA",
                LocalDate.of(LocalDate.now().getYear() + 1, 1, 1),
                888);
        PaymentRequest paymentRequest = new PaymentRequest(amount, card);

        userService.topUpBalance(paymentRequest, user.getId());
        User updatedUser = userService.findById(user.getId());

        assertEquals(balance.add(amount), updatedUser.getBalance());
    }
}

