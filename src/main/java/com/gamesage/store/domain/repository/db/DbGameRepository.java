package com.gamesage.store.domain.repository.db;

import com.gamesage.store.GameTierra;
import com.gamesage.store.dao.DbInitializer;
import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.repository.GameRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Repository
public class DbGameRepository extends GameRepository {

    private JdbcTemplate jdbcTemplate;
    private DbInitializer dbInitializer;

    public DbGameRepository(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
        dbInitializer = new DbInitializer(this.jdbcTemplate);
        Locale.setDefault(Locale.US);
    }

    @Override
    public List<Game> create(List<Game> gamesToAdd) {

        for (Game game : gamesToAdd){
            String sqlCommand = String.format(
                    "INSERT INTO game " +
                    "VALUES (%d, \' %s \', %,.2f)\n",
                    game.getId(), game.getName(), game.getPrice());
            jdbcTemplate.execute(sqlCommand);
        }
        return gamesToAdd;
    }

    @Override
    public List<Game> findAll() {

        List<Game> retrievedGames = new ArrayList<>();

        List<Game> results = (List<Game>) jdbcTemplate.query(
                "select * from game"
                , new Object[] { "aGame" },
                (RowMapper<Game>) (rs, rowNum) -> new Game(rs.getInt("id"), rs.getString("name"),
                        BigDecimal.valueOf(rs.getDouble("price"))));

        for (Game game : results) {
            GameTierra.logger.warn(String.valueOf(game));
        }
        return retrievedGames;
    }
}

