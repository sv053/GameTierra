package com.gamesage.store.domain.repository.db;

import com.gamesage.store.GameTierra;
import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.repository.CreateManyRepository;
import com.gamesage.store.exception.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@org.springframework.stereotype.Repository
public class DbGameRepository implements CreateManyRepository<Game, Integer> {

    private JdbcTemplate jdbcTemplate;
    private List<Game> games;

    public DbGameRepository(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
        games = new ArrayList<>();
        Locale.setDefault(Locale.US);
    }

    @Override
    public Optional<Game> findById(Integer id) throws EmptyResultDataAccessException {

        Game queryResult = (Game) jdbcTemplate.queryForObject(
                "SELECT * FROM game WHERE ID = " + id
                , new GameRowMapper());

        return Optional.ofNullable(queryResult);
    }

    @Override
    public List<Game> findAll() {

        List<Game> results = jdbcTemplate.query(
                "SELECT * FROM game "
                , new GameRowMapper());
        if(results.isEmpty()) GameTierra.logger.error("table GAME is empty");
        else games = results;
        Arrays.asList(results).forEach(g -> GameTierra.logger.info(g.toString()));

        return results;
    }

    @Override
    public Game createOne(Game gameToAdd) {
        StringBuilder sqlCommand = new StringBuilder();
        sqlCommand.append("INSERT INTO game VALUES ");
        sqlCommand.append(String.format("(%d, \' %s \', %,.2f)\n",
                gameToAdd.getId(), gameToAdd.getName(), gameToAdd.getPrice()));
        jdbcTemplate.execute(sqlCommand.toString());

        return gameToAdd;
    }

    @Override
    public List<Game> create(List<Game> gamesToAdd) {
        StringBuilder sqlCommand = new StringBuilder();
        sqlCommand.append("INSERT INTO game VALUES ");
        for (Game game : gamesToAdd){
            String value = String.format("(%d, \' %s \', %,.2f)\n",
                    game.getId(), game.getName(), game.getPrice());
            sqlCommand.append(value);
            if(!gamesToAdd.get(gamesToAdd.size()-1).equals(game))
                sqlCommand.append(",");
        }
        jdbcTemplate.execute(sqlCommand.toString());

        return gamesToAdd;
    }

    public class GameRowMapper implements RowMapper<Game> {

        @Override
        public Game mapRow(ResultSet rs, int rowNum) throws SQLException {

            Game game = new Game(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getBigDecimal("price"));

            return game;
        }
    }
}

