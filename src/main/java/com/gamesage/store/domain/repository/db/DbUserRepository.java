package com.gamesage.store.domain.repository.db;

import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.Repository;
import com.gamesage.store.exception.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@org.springframework.stereotype.Repository
public class DbUserRepository implements Repository<User, Integer> {

    private JdbcTemplate jdbcTemplate;
    private List<User> users;

    public DbUserRepository(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
        users = new ArrayList<>();
        Locale.setDefault(Locale.US);
    }

    @Override
    public Optional<User> findById(Integer id) throws EmptyResultDataAccessException {

        UserRowCallbackHandler rowCallbackHandler = new UserRowCallbackHandler();
        jdbcTemplate.query(
                "SELECT * FROM user " +
                        "LEFT JOIN tier " +
                        "on user.tier_id = tier.id " +
                        "WHERE user.id = ? ",
                rowCallbackHandler,
                id
        );
        User user = new ArrayList<>(rowCallbackHandler.getUsers()).get(0);
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findAll() {

        UserRowCallbackHandler rowCallbackHandler = new UserRowCallbackHandler();
        jdbcTemplate.query(
                "SELECT * FROM user " +
                        "LEFT JOIN tier " +
                        "on user.tier_id = tier.id",
                rowCallbackHandler
        );
        return new ArrayList<>(rowCallbackHandler.getUsers());
    }

    @Override
    public User createOne(User userToAdd) {
        StringBuilder sqlCommand = new StringBuilder();
        sqlCommand.append("INSERT INTO user VALUES ");
        sqlCommand.append(String.format("(%d, \' %s \', %d, %,.2f)\n",
                userToAdd.getId(), userToAdd.getLogin(), userToAdd.getTier().getId(), userToAdd.getBalance()));
        jdbcTemplate.execute(sqlCommand.toString());

        return userToAdd;
    }

    class UserRowCallbackHandler implements RowCallbackHandler {
        private Collection<User> users = new ArrayList<>();
        private User currentUser = null;
        private Tier tier = null;

        public Collection<User> getUsers() {
            return users;
        }

        public void processRow(ResultSet rs) throws SQLException {
            int userId = rs.getInt("id");
            if (currentUser == null || userId != currentUser.getId()) {

                Integer id = rs.getInt(userId);
                String login = rs.getString("login");
                BigDecimal balance = rs.getBigDecimal("balance");
                tier = new Tier(rs.getInt("tier_id"),
                        rs.getString("name"),
                        rs.getDouble("percentage"));

                currentUser = new User(id, login, tier, balance);
                users.add(currentUser);
            }
        }
    }
}

