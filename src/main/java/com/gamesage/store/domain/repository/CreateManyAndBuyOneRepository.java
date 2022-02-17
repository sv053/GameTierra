package com.gamesage.store.domain.repository;

import com.gamesage.store.domain.model.Order;

import java.util.List;

public interface CreateManyAndBuyOneRepository<T, K> extends Repository<T, K> {
    List<T> create(List<T> items);

    Order createOrder(Order order);
}

