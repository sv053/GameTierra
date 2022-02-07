package com.gamesage.store.domain.repository.db;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.repository.CreateManyRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class DbGameRepository implements CreateManyRepository<Game, Integer> {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Game> gameRowMapper;

    public DbGameRepository(JdbcTemplate jdbcTemplate, GameRowMapper gameRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.gameRowMapper = gameRowMapper;
    }

    @Override
    public Optional<Game> findById(Integer id) {
        Optional<Game> result;
        try {
            result = Optional.ofNullable(jdbcTemplate.queryForObject(
                    "SELECT * FROM game WHERE ID = ?"
                    , gameRowMapper
                    , id));
        } catch (Exception e) {
            return Optional.empty();
        }
        return result;
    }

    @Override
    public List<Game> findAll() {
        List<Game> results = jdbcTemplate.query(
                "SELECT * FROM game "
                , gameRowMapper);

        return results;
    }

    @Override
    @Transactional
    public Game createOne(Game gameToAdd) throws SQLException {
        jdbcTemplate.update("INSERT INTO game VALUES(?,?,?) ",
                gameToAdd.getId(),
                gameToAdd.getName(),
                gameToAdd.getPrice());

        Game game = findById(gameToAdd.getId())
                .orElseThrow(SQLException::new);
        return game;
    }

    @Override
    @Transactional
    public List<Game> create(List<Game> gamesToAdd) throws SQLException {
        List<Game> games = new ArrayList<>();
        for (Game game : gamesToAdd) {
            games.add(createOne(game));
        }
        return games;
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

