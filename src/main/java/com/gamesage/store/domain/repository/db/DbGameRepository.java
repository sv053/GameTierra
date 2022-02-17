package com.gamesage.store.domain.repository.db;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.Order;
import com.gamesage.store.domain.repository.CreateManyAndBuyOneRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class DbGameRepository implements CreateManyAndBuyOneRepository<Game, Integer> {

    private static final String SELECT_GAME_QUERY = "SELECT id, name, price FROM game";
    private static final String INSERT_GAME_QUERY = "INSERT into game (name, price) VALUES (?, ?) ";
    private static final String INSERT_ORDER = "INSERT into user_game (user_id, game_id, order_date) VALUES (?, ?, ?) ";
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Game> gameRowMapper;

    public DbGameRepository(JdbcTemplate jdbcTemplate, RowMapper<Game> gameRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.gameRowMapper = gameRowMapper;
    }

    @Override
    public Optional<Game> findById(Integer id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    SELECT_GAME_QUERY + " WHERE ID = ?"
                    , gameRowMapper
                    , id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Game> findAll() {
        return jdbcTemplate.query(SELECT_GAME_QUERY, gameRowMapper);
    }

    @Override
    public Game createOne(Game gameToAdd) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con
                    .prepareStatement(INSERT_GAME_QUERY,
                            Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, gameToAdd.getName());
            ps.setBigDecimal(2, gameToAdd.getPrice());
            return ps;
        }, keyHolder);
        return new Game(keyHolder.getKeyAs(Integer.class),
                gameToAdd.getName(),
                gameToAdd.getPrice());
    }

    @Override
    public int update(Game item) {
        return 0;
    }

    @Override
    @Transactional
    public List<Game> create(List<Game> gamesToAdd) {
        List<Game> addedGames = new ArrayList<>();
        gamesToAdd.forEach(g -> addedGames.add(createOne(g)));
        return addedGames;
    }

    @Override
    public Order createOrder(Order orderToAdd) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con
                    .prepareStatement(INSERT_ORDER,
                            Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, orderToAdd.getUser().getId());
            ps.setInt(2, orderToAdd.getGame().getId());
            ps.setDate(3, java.sql.Date.valueOf(orderToAdd.getDate().toString()));
            return ps;
        }, keyHolder);
        return new Order(keyHolder.getKeyAs(Integer.class),
                orderToAdd.getUser(),
                orderToAdd.getGame(),
                orderToAdd.getDate());
    }

    @Component
    static class GameRowMapper implements RowMapper<Game> {

        @Override
        public Game mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Game(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getBigDecimal("price"));
        }
    }
}

