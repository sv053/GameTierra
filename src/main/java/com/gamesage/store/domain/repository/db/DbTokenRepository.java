package com.gamesage.store.domain.repository.db;

import com.gamesage.store.domain.repository.TokenRepository;
import com.gamesage.store.domain.repository.model.AuthTokenEntity;
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

    private static final String SELECT_TOKEN_BY_USERID_QUERY = "SELECT token_value " +
            "FROM token " +
            "WHERE user_id = ?";
    private static final String SELECT_TOKEN_QUERY = "SELECT user_id, token_value " +
            "FROM token " +
            "WHERE token_value = ?";
    private static final String INSERT_USER_TOKEN = "INSERT INTO token (token_value, user_id) " +
            " VALUES (?, ?) ";
    private static final String DELETE_TOKEN = "DELETE FROM token WHERE user_id = ?";
    private final JdbcTemplate jdbcTemplate;
    private final TokenRowMapper tokenRowMapper;

    public DbTokenRepository(JdbcTemplate jdbcTemplate, TokenRowMapper tokenRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.tokenRowMapper = tokenRowMapper;
    }

    @Override
    public Optional<AuthTokenEntity> retrieveByUserId(int userId) {
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
    public Optional<AuthTokenEntity> retrieveByValue(String token) {
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
    public AuthTokenEntity persistToken(AuthTokenEntity authTokenEntity) {
        jdbcTemplate.update(DELETE_TOKEN, authTokenEntity.getUserId());
        jdbcTemplate.update(INSERT_USER_TOKEN,
                authTokenEntity.getValue(),
                authTokenEntity.getUserId());
        return authTokenEntity;
    }

    @Component
    static class TokenRowMapper implements RowMapper<AuthTokenEntity> {

        @Override
        public AuthTokenEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new AuthTokenEntity(
                    rs.getInt("user_id"),
                    rs.getString("token_value")
            );
        }
    }
}

