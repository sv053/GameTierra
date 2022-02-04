package com.gamesage.store.domain.repository.db;

import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.Repository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@org.springframework.stereotype.Repository
public class DbUserRepository implements Repository<User, Integer> {

    private JdbcTemplate jdbcTemplate;
    private UserRowMapper userRowMapper;

    public DbUserRepository(JdbcTemplate jdbcTemplate, UserRowMapper userRowMapper) {

        this.jdbcTemplate = jdbcTemplate;
        this.userRowMapper = userRowMapper;
        Locale.setDefault(Locale.US);
    }

    @Override
    public Optional<User> findById(Integer id) {

        User user = null;
        try {
            user = jdbcTemplate.queryForObject(
                    "SELECT * FROM user " +
                            "LEFT JOIN tier " +
                            "on user.tier_id = tier.id " +
                            "WHERE user.id = " + id,
                        userRowMapper
            );
        } catch (EmptyResultDataAccessException e) {
        }
        return Optional.ofNullable(user);
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
    public User createOne(User userToAdd) {

        String query = "insert into user values(?,?,?,?)";
        try {
            jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
                @Override
                public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException {
                    ps.setInt(1, userToAdd.getId());
                    ps.setString(2, userToAdd.getLogin());
                    ps.setBigDecimal(3, userToAdd.getBalance());
                    ps.setInt(4, userToAdd.getTier().getId());
                    return ps.execute();
                }
            });
        } catch (DuplicateKeyException e) {
            throw new DuplicateKeyException("Unique index or primary key violation");
        }
        return userToAdd;
    }
}

@Service
class UserRowMapper implements RowMapper<User> {

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

