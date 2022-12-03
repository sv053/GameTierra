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
import java.util.Optional;

@Repository
public class DbTokenRepository implements TokenRepository {

    private static final String SELECT_TOKEN_BY_USERID_QUERY = "SELECT token_value, user_login " +
            " FROM token " +
            " WHERE user_id = ? ";
    private static final String SELECT_TOKEN_BY_USER_LOGIN = "SELECT token_value, user_login " +
            " FROM token " +
            " WHERE user_login = ? ";
    private static final String SELECT_TOKEN_QUERY = "SELECT token_value, user_login " +
            " FROM token " +
            " WHERE token_value = ? ";
    private static final String INSERT_USER_TOKEN = "INSERT INTO token (token_value, user_login) " +
            " VALUES (?, ?) ";
    private final JdbcTemplate jdbcTemplate;
    private final TokenRowMapper tokenRowMapper;

    public DbTokenRepository(JdbcTemplate jdbcTemplate, TokenRowMapper tokenRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.tokenRowMapper = tokenRowMapper;
    }

    @Override
    public Optional<AuthToken> findByUserLogin(String login) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    SELECT_TOKEN_BY_USER_LOGIN,
                    tokenRowMapper,
                    login
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
    public AuthToken saveToken(AuthToken AuthToken) {
        jdbcTemplate.update(INSERT_USER_TOKEN,
                AuthToken.getValue(),
                AuthToken.getUserLogin());
        return AuthToken;
    }

    @Component
    static class TokenRowMapper implements RowMapper<AuthToken> {

        @Override
        public AuthToken mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new AuthToken(
                    rs.getString("token_value"),
                    rs.getString("user_login"));
        }
    }
}

