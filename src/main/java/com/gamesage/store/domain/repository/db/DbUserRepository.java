package com.gamesage.store.domain.repository.db;

import com.gamesage.store.GameTierra;
import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DbUserRepository implements Repository {

    private JdbcTemplate jdbcTemplate;

    public DbUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional findById(Object id) {
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        List<User> retrievedUsers = new ArrayList<>();

        List<User> results = (List<User>) jdbcTemplate.query(
                "SELECT id, login, balance FROM user"
                , new Object[] { "User" },
                (RowMapper<User>) (rs, rowNum) -> new User(rs.getInt("id"), rs.getString("login"),
                        BigDecimal.valueOf(rs.getDouble("balance"))));

        Arrays.asList(results).forEach(g -> GameTierra.logger.info(g.toString()));
        return retrievedUsers;
    }

    @Override
    public Object createOne(Object item) {
        return null;
    }
}
