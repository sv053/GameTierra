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
import java.util.List;
import java.util.Optional;

@Repository
public class DbGameRepository implements CreateManyRepository<Game, Integer> {

    private static final String selectGameQuery = "SELECT id, name, price FROM game";
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
                    selectGameQuery + " WHERE ID = ?"
                    , gameRowMapper
                    , id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Game> findAll() {
        return jdbcTemplate.query(selectGameQuery, gameRowMapper);
    }

    @Override
    public Game createOne(Game gameToAdd) {
        String INSERT_MESSAGE = "insert into game (name, price) values( ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con
                    .prepareStatement(INSERT_MESSAGE,
                            Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, gameToAdd.getName());
            ps.setBigDecimal(2, gameToAdd.getPrice());
            return ps;
        }, keyHolder);
        return new Game(keyHolder.getKeyAs(Integer.TYPE),
                gameToAdd.getName(),
                gameToAdd.getPrice());
    }

    @Override
    @Transactional
    public List<Game> create(List<Game> gamesToAdd) {
        List<Game> addedGames = new ArrayList<>();
        gamesToAdd.forEach(g -> addedGames.add(createOne(g)));
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

