package com.gamesage.store.domain.repository.db;

import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.Repository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
class DbUserRepository implements Repository<User, Integer> {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<User> userRowMapper;

    public DbUserRepository(JdbcTemplate jdbcTemplate, UserRowMapper userRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRowMapper = userRowMapper;
    }

    @Override
    public Optional<User> findById(Integer id) {
        Optional<User> result;
        try {
            result = Optional.ofNullable(jdbcTemplate.queryForObject(
                    "SELECT * FROM user " +
                            "LEFT JOIN tier " +
                            "on user.tier_id = tier.id " +
                            "WHERE user.id = ?",
                    userRowMapper,
                    id
            ));
        } catch (EmptyResultDataAccessException e) {
            result = Optional.empty();
        }
        return result;
    }

    @Override
    public List<User> findAll() {
        List<User> users = jdbcTemplate.query(
                "SELECT * FROM user " +
                        "LEFT JOIN tier " +
                        "on user.tier_id = tier.id",
                userRowMapper
        );
        return users;
    }

    @Override
    @Transactional
    public User createOne(User userToAdd) throws SQLException {
        jdbcTemplate.update("insert into user values(?,?,?,?)",
                userToAdd.getId(),
                userToAdd.getLogin(),
                userToAdd.getBalance(),
                userToAdd.getTier().getId());
        return findById(userToAdd.getId()).orElseThrow(SQLException::new);
    }


    @Component
    static class UserRowMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            Tier tier = new Tier(
                    rs.getInt(5),
                    rs.getString(6),
                    rs.getDouble(7)
            );
            User user = new User(
                    rs.getInt("id"),
                    rs.getString(2),
                    tier,
                    rs.getBigDecimal("balance"));

            return user;
        }
    }
}

