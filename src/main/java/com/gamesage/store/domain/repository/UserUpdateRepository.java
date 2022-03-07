package com.gamesage.store.domain.repository;

import com.gamesage.store.domain.model.Card;
import com.gamesage.store.domain.model.User;

import java.math.BigDecimal;

public interface UserUpdateRepository extends Repository<User, Integer> {

    User update(User user);

    User updateUserBalance(User user);

    Card validateCard(Card card, BigDecimal amount);
}

