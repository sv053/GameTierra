package com.gamesage.store.domain.repository.db;

import com.gamesage.store.domain.repository.TokenRepository;
import com.gamesage.store.security.model.AuthToken;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class DbTokenRepository implements TokenRepository {

    private static final String SELECT_TOKEN_BY_USERID_QUERY = "SELECT token_value, user_id " +
            " FROM token " +
            " WHERE user_id = ? ";
    private static final String SELECT_TOKEN_QUERY = "SELECT token_value, user_id " +
            " FROM token " +
            " WHERE token_value = ? ";
    private static final String INSERT_USER_TOKEN = " INTO token (token_value, user_id) " +
            " VALUES (?, ?) ";
    private final JdbcTemplate jdbcTemplate;
    private final TokenRowMapper tokenRowMapper;

    public DbTokenRepository(JdbcTemplate jdbcTemplate, TokenRowMapper tokenRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.tokenRowMapper = tokenRowMapper;
    }

    @Override
    public Optional<AuthToken> retrieveByUserId(int userId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    SELECT_TOKEN_BY_USERID_QUERY,
                    tokenRowMapper,
                    userId
            ));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<AuthToken> retrieveByValue(String token) {
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
    public AuthToken persistToken(AuthToken authToken) {
        jdbcTemplate.update(INSERT_USER_TOKEN,
                authToken.getValue(),
                authToken.getUserId());
        return authToken;
    }

    @Component
    static class TokenRowMapper implements RowMapper<AuthToken> {

        @Override
        public AuthToken mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new AuthToken(
                    rs.getString("token_value"),
                    rs.getInt("user_id")
            );
        }
    }
}

