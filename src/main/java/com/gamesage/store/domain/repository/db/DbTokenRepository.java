package com.gamesage.store.domain.repository.db;

import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.domain.repository.TokenRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.*;
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
    private static final String SELECT_TOKEN_BY_ID = "SELECT id, token_value, user_id, expiration_date " +
            " FROM token " +
            " WHERE id = ? ";
    private static final String SELECT_TOKEN_QUERY = "SELECT token_value, user_id, expiration_date " +
            " FROM token " +
            " WHERE token_value = ? ";
    private static final String INSERT_USER_TOKEN = "INSERT INTO token (token_value, user_id, expiration_date) " +
            " VALUES (?, ?, ?) ";
    private static final String UPDATE_TOKEN = "UPDATE token SET token_value = ? " +
            "WHERE id = ?";
    private static final String REMOVE_EXPIRED_TOKENS = "DELETE " +
            " FROM token " +
            " WHERE expiration_date < ? ";
    private static final String REMOVE_TOKEN = "DELETE " +
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
                    SELECT_TOKEN_BY_ID,
                    tokenRowMapper,
                    id
            ));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<AuthToken> findByValue(String token) {
        String tokenId = "0";
//        tokenId = token.split("\u001C")[0] ;
        tokenId = token.split("___")[0];
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    SELECT_TOKEN_BY_ID,
                    tokenRowMapper,
                    tokenId
            ));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public AuthToken createOne(AuthToken authToken) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con
                    .prepareStatement(INSERT_USER_TOKEN,
                            Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, authToken.getValue());
            ps.setInt(2, authToken.getUserId());
            ps.setTimestamp(3, Timestamp.valueOf(authToken.getExpirationDateTime()));
            return ps;
        }, keyHolder);

        Integer id = keyHolder.getKeyAs(Integer.class);
        String tokenWithItsId = new StringBuilder()
                .append(id)
//                .append((char) 0x1C)
                .append("___")
                .append(authToken.getValue())
                .toString();
        jdbcTemplate.update(UPDATE_TOKEN
                , tokenWithItsId
                , id);
        return new AuthToken(
                id,
                tokenWithItsId,
                authToken.getUserId(),
                authToken.getExpirationDateTime());
    }

    @Override
    public void removeExpired() {
        jdbcTemplate.update(REMOVE_EXPIRED_TOKENS, LocalDateTime.now());
    }

    @Override
    public void removeByValue(String token) {
        jdbcTemplate.update(REMOVE_TOKEN, token);
    }

    @Component
    static class TokenRowMapper implements RowMapper<AuthToken> {

        @Override
        public AuthToken mapRow(ResultSet rs, int rowNum) throws SQLException {
            Timestamp timestamp = rs.getTimestamp("expiration_date");
            LocalDateTime dateTime = timestamp.toLocalDateTime();
            return new AuthToken(
                    rs.getInt("id"),
                    rs.getString("token_value"),
                    rs.getInt("user_id"),
                    dateTime);
        }
    }
}

