package com.gamesage.store.domain.repository.db;

import com.gamesage.store.domain.model.Order;
import com.gamesage.store.domain.repository.CreateOneRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;

@org.springframework.stereotype.Repository
public class DbOrderRepository implements CreateOneRepository<Order, Integer> {

    private static final String INSERT_ORDER = "INSERT INTO orders (user_id, game_id, order_datetime) " +
                                                    "VALUES (?, ?, ?) ";
    private final JdbcTemplate jdbcTemplate;

    public DbOrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Order createOne(Order orderToAdd) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con
                    .prepareStatement(INSERT_ORDER,
                            Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, orderToAdd.getUser().getId());
            ps.setInt(2, orderToAdd.getGame().getId());
            ps.setTimestamp(3, Timestamp.valueOf(orderToAdd.getDateTime()));
            return ps;
        }, keyHolder);
        return new Order(keyHolder.getKeyAs(Integer.class),
                orderToAdd.getUser(),
                orderToAdd.getGame(),
                orderToAdd.getDateTime());
    }
}

