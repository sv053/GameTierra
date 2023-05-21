package com.gamesage.store.domain.repository.db;

import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.domain.repository.TokenRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class DbTokenRepository implements TokenRepository {

    private static final String SELECT_ALL_TOKENS_QUERY = "SELECT token_value, user_id, expiration_date " +
            " FROM token ";
    private static final String SELECT_TOKEN_BY_USER_ID = "SELECT token_value, user_id, expiration_date " +
            " FROM token " +
            " WHERE user_id = ? ";
    private static final String SELECT_TOKEN_QUERY = "SELECT token_value, user_id, expiration_date " +
            " FROM token " +
            " WHERE token_value = ? ";
    private static final String INSERT_USER_TOKEN = "INSERT INTO token (token_value, user_id, expiration_date) " +
            " VALUES (?, ?, ?) ";
    private static final String REMOVE_EXPIRED_TOKENS = "DELETE " +
            " FROM token " +
            " WHERE expiration_date < ? ";
    private static final String REMOVE_EXPIRED_TOKEN = "DELETE " +
            " FROM token " +
            " WHERE token_value = ? ";
    private final JdbcTemplate jdbcTemplate;
    private final TokenRowMapper tokenRowMapper;

    public DbTokenRepository(JdbcTemplate jdbcTemplate, TokenRowMapper tokenRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.tokenRowMapper = tokenRowMapper;
    }

    @Override
    public List<AuthToken> findAll() {
        return jdbcTemplate.query(SELECT_ALL_TOKENS_QUERY, tokenRowMapper);
    }

    @Override
    public Optional<AuthToken> findById(Integer id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    SELECT_TOKEN_BY_USER_ID,
                    tokenRowMapper,
                    id
            ));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<AuthToken> findByValue(String token) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    SELECT_TOKEN_QUERY,
                    tokenRowMapper,
                    token
            ));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public AuthToken createOne(AuthToken authToken) {
        jdbcTemplate.update(INSERT_USER_TOKEN,
                authToken.getValue(),
                authToken.getUserId(),
                Timestamp.valueOf(authToken.getExpirationDate()));
        return authToken;
    }

    @Override
    public void removeExpired() {
        jdbcTemplate.update(REMOVE_EXPIRED_TOKENS, LocalDateTime.now());
    }

    @Override
    public void removeByValue(String token) {
        jdbcTemplate.update(REMOVE_EXPIRED_TOKEN, token);
    }

    @Component
    static class TokenRowMapper implements RowMapper<AuthToken> {

        @Override
        public AuthToken mapRow(ResultSet rs, int rowNum) throws SQLException {
            Timestamp timestamp = rs.getTimestamp("expiration_date");
            LocalDateTime dateTime = timestamp.toLocalDateTime();
            return new AuthToken(
                    rs.getString("token_value"),
                    rs.getInt("user_id"),
                    dateTime);
        }
    }
}

