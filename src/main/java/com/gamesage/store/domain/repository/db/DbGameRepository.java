package com.gamesage.store.domain.repository.db;

import com.gamesage.store.GameTierra;
import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.repository.CreateManyRepository;
import com.gamesage.store.exception.EntityNotFoundException;
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
public class DbGameRepository implements CreateManyRepository<Game, Integer> {

    private JdbcTemplate jdbcTemplate;
    private GameRowMapper gameRowMapper;

    public DbGameRepository(JdbcTemplate jdbcTemplate, GameRowMapper gameRowMapper) {

        this.jdbcTemplate = jdbcTemplate;
        this.gameRowMapper = gameRowMapper;
        Locale.setDefault(Locale.US);
    }

    @Override
    public Optional<Game> findById(Integer id) {

        Game result = null;
        try {
            result = (Game) jdbcTemplate.queryForObject(
                    "SELECT * FROM game WHERE ID = " + id
                    , gameRowMapper);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(id);
        }
        return Optional.ofNullable(result);
    }

    @Override
    public List<Game> findAll() {

        List<Game> results = jdbcTemplate.query(
                "SELECT * FROM game "
                , gameRowMapper);
        if (results.isEmpty()) GameTierra.logger.warn("table GAME is empty");

        return results;
    }

    @Override
    public Game createOne(Game gameToAdd) {

        String query = "INSERT INTO game VALUES ";
        jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException {
                ps.setInt(1, gameToAdd.getId());
                ps.setString(2, gameToAdd.getName());
                ps.setBigDecimal(3, gameToAdd.getPrice());
                return ps.execute();
            }
        });
        return gameToAdd;
    }

    @Override
    public List<Game> create(List<Game> gamesToAdd) {

        for (Game game : gamesToAdd) {
            createOne(game);
        }
        return gamesToAdd;
    }

}

@Service
class GameRowMapper implements RowMapper<Game> {

    @Override
    public Game mapRow(ResultSet rs, int rowNum) throws SQLException {

        Game game = new Game(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getBigDecimal("price"));

        return game;
    }
}

