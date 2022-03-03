package com.gamesage.store.domain.repository.db;

import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.UserUpdateRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class DbUserRepository implements UserUpdateRepository {

    private static final String SELECT_USER_QUERY = "SELECT user.id AS user_id, login, balance, " +
            "tier_id, name AS tl, tier.percentage AS tp FROM user " +
            "LEFT JOIN tier " +
            "on user.tier_id = tier.id ";
    private static final String INSERT_USER_QUERY = "INSERT INTO user (login, balance, tier_id) " +
            "VALUES ( ?, ?, ?) ";
    private static final String UPDATE_USER = "UPDATE user SET balance = ?, tier_id = ?  " +
            "WHERE id = ? ";
    private static final String UPDATE_USER_BALANCE = "UPDATE user SET balance = ? " +
            "WHERE id = ?";
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<User> userRowMapper;

    public DbUserRepository(JdbcTemplate jdbcTemplate, UserRowMapper userRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRowMapper = userRowMapper;
    }

    @Override
    public Optional<User> findById(Integer id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    SELECT_USER_QUERY +
                            "WHERE user.id = ?",
                    userRowMapper,
                    id
            ));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(SELECT_USER_QUERY, userRowMapper);
    }

    @Override
    public User createOne(User userToAdd) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(INSERT_USER_QUERY,
                    new String[]{"id"});
            ps.setString(1, userToAdd.getLogin());
            ps.setBigDecimal(2, userToAdd.getBalance());
            ps.setInt(3, userToAdd.getTier().getId());
            return ps;
        }, keyHolder);
        return new User(keyHolder.getKeyAs(Integer.class),
                userToAdd.getLogin(),
                userToAdd.getTier(),
                userToAdd.getBalance());
    }

    @Override
    public User update(User userToUpdate) {
        jdbcTemplate.update(UPDATE_USER
                , userToUpdate.getBalance()
                , userToUpdate.getTier().getId()
                , userToUpdate.getId());
        return userToUpdate;
    }

    @Override
    public User updateUserBalance(User userToUpdate) {
        jdbcTemplate.update(UPDATE_USER_BALANCE
                , userToUpdate.getBalance()
                , userToUpdate.getId());
        return userToUpdate;
    }

    @Component
    static class UserRowMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            Tier tier = new Tier(
                    rs.getInt("tier_id"),
                    rs.getString("tl"),
                    rs.getDouble("tp")
            );
            return new User(
                    rs.getInt("user_id"),
                    rs.getString("login"),
                    tier,
                    rs.getBigDecimal("balance"));
        }
    }
}

