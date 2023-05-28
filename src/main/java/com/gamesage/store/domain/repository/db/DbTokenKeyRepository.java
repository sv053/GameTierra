package com.gamesage.store.domain.repository.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DbTokenKeyRepository {

    private static final String SELECT_TOKEN_KEY_QUERY = "SELECT key_value " +
            " FROM token_key " +
            " WHERE key_value <> null ";
    private static final String INSERT_TOKEN_KEY = "INSERT INTO token_key (key_value) " +
            " VALUES (?) ";
    private final JdbcTemplate jdbcTemplate;

    public DbTokenKeyRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String findKey() {
        return jdbcTemplate.queryForObject(SELECT_TOKEN_KEY_QUERY, String.class);
    }

    public String createOne(String key) {
        jdbcTemplate.update(INSERT_TOKEN_KEY, key);
        return key;
    }

//    public void removeExpired() {
//        jdbcTemplate.update(REMOVE_EXPIRED_TOKENS, LocalDateTime.now());
//    }

}

