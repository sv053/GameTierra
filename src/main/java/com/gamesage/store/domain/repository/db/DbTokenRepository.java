package com.gamesage.store.domain.repository.db;

import com.gamesage.store.domain.repository.TokenRepository;
import com.gamesage.store.security.model.AuthToken;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class DbTokenRepository implements TokenRepository {

    private static final String SELECT_TOKEN_BY_USERID_QUERY = "SELECT id, token, " +
            "FROM auth_token " +
            "WHERE userId = ?";
    private static final String SELECT_TOKEN_QUERY = "SELECT id, userId, " +
            "FROM auth_token " +
            "WHERE token = ?";
    private static final String INSERT_USER_TOKEN = "INSERT INTO auth_token (id, token, user_id) " +
            " VALUES (?, ?, ?) ";
    private final JdbcTemplate jdbcTemplate;
    private final BCryptPasswordEncoder encoder;
    private final TokenRowMapper tokenRowMapper;

    public DbTokenRepository(JdbcTemplate jdbcTemplate, BCryptPasswordEncoder encoder, TokenRowMapper tokenRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.encoder = encoder;
        this.tokenRowMapper = tokenRowMapper;
    }

    @Override
    public Optional<AuthToken> findToken(int userId) {
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
    public Optional<AuthToken> findToken(String token) {
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
    public AuthToken createToken(int userId) {
        AuthToken token = new AuthToken(userId);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(INSERT_USER_TOKEN,
                    new String[]{"id"});
            ps.setString(1, token.getToken());
            ps.setInt(2, token.getUserId());
            return ps;
        }, keyHolder);
        token.setId((Long) keyHolder.getKey());
        return token;
    }

    @Component
    static class TokenRowMapper implements RowMapper<AuthToken> {

        @Override
        public AuthToken mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new AuthToken(
                    rs.getLong("id"),
                    rs.getString("token"),
                    rs.getInt("user_id")
            );
        }
    }
}

