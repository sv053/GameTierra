package com.gamesage.store.domain.repository.db;

import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.Repository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
class DbUserRepository implements Repository<User, Integer> {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<User> userRowMapper;
    private final String findUserQuery;

    public DbUserRepository(JdbcTemplate jdbcTemplate, UserRowMapper userRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRowMapper = userRowMapper;
        findUserQuery = "SELECT user.id AS user_id, login, balance, " +
                "tier_id, level AS tl, tier.percentage AS tp FROM user " +
                "LEFT JOIN tier " +
                "on user.tier_id = tier.id ";
    }

    @Override
    public Optional<User> findById(Integer id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    findUserQuery +
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
        return jdbcTemplate.query(
                findUserQuery,
                userRowMapper
        );
    }

    @Override
    @Transactional
    public User createOne(User userToAdd) {
        String INSERT_MESSAGE = "insert into user (login, balance, tier_id) values ( ?, ?, ?) ";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(INSERT_MESSAGE,
                    new String[]{"id"});
            ps.setString(1, userToAdd.getLogin());
            ps.setBigDecimal(2, userToAdd.getBalance());
            ps.setInt(3, userToAdd.getTier().getId());
            return ps;
        }, keyHolder);
        userToAdd.setId((Integer) keyHolder.getKey());
        return userToAdd;
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

