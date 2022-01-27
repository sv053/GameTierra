package com.gamesage.store.domain.repository.db;

import com.gamesage.store.GameTierra;
import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.repository.CreateManyRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.*;

@Repository
public class DbGameRepository implements CreateManyRepository<Game, Integer> {

    private JdbcTemplate jdbcTemplate;

    public DbGameRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {

        this.jdbcTemplate = jdbcTemplate;

        Locale.setDefault(Locale.US);
    }

    @Override
    public Optional<Game> findById(Integer id) {
       return null;
    }

    @Override
    public List<Game> findAll() {
        List<Game> retrievedGames = new ArrayList<>();

        List<Game> results = (List<Game>) jdbcTemplate.query(
                "SELECT * FROM game"
                , new Object[] { "Game" },
                (RowMapper<Game>) (rs, rowNum) -> new Game(rs.getInt("id"), rs.getString("name"),
                        BigDecimal.valueOf(rs.getDouble("price"))));

        Arrays.asList(results).forEach(g -> GameTierra.logger.info(g.toString()));
        return retrievedGames;
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
}

