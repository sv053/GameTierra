package com.gamesage.store.domain.repository.db;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.repository.CreateManyRepository;
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
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Repository
public class DbGameRepository implements CreateManyRepository<Game, Integer> {

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
                    "SELECT id, name, price FROM game WHERE ID = ?"
                    , gameRowMapper
                    , id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Game> findAll() {
        List<Game> results = jdbcTemplate.query(
                "SELECT id, name, price FROM game "
                , gameRowMapper);

        return results;
    }

    @Override
    @Transactional
    public Game createOne(Game gameToAdd) {
        String INSERT_MESSAGE = "insert into game (name, price) values( ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con
                    .prepareStatement(INSERT_MESSAGE,
                            new String[]{"id"});
            ps.setString(1, gameToAdd.getName());
            ps.setBigDecimal(2, gameToAdd.getPrice());
            return ps;
        }, keyHolder);
        gameToAdd.setId((Integer) keyHolder.getKey());
        return gameToAdd;
    }

    @Override
    @Transactional
    public List<Game> create(List<Game> gamesToAdd) {
        List<Game> addedGames = new ArrayList<>();
        String INSERT_MESSAGE = "insert into game (name, price) values( ?, ? ) ";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        Iterator<Game> gameIterator = gamesToAdd.listIterator();
        while (gameIterator.hasNext()) {
            Game currentGame = gameIterator.next();
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(INSERT_MESSAGE,
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, currentGame.getName());
                ps.setBigDecimal(2, currentGame.getPrice());
                ResultSet rs = ps.getGeneratedKeys();
                return ps;
            }, keyHolder);
            currentGame.setId((Integer) keyHolder.getKey());
            addedGames.add(currentGame);
        }
        return addedGames;
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

