package com.gamesage.store.domain.repository.db;

import com.gamesage.store.GameTierra;
import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.repository.DbRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;

@Repository
public class DbGameRepository implements DbRepository<Game, Integer> {

    private DataSourceInit dataSourceInit;

    public DbGameRepository(DataSourceInit dataSourceInit) {

        this.dataSourceInit = dataSourceInit;

        Locale.setDefault(Locale.US);
    }

    @Override
    public List<Game> insertAll(List<Game> gamesToAdd) {

        StringBuilder sqlCommand = new StringBuilder();
        sqlCommand.append("INSERT INTO game VALUES ");
        for (Game game : gamesToAdd){
            String value = String.format("(%d, \' %s \', %,.2f)\n",
                    game.getId(), game.getName(), game.getPrice());
            sqlCommand.append(value);
            if(!gamesToAdd.get(gamesToAdd.size()-1).equals(game))
                sqlCommand.append(",");
        }
        dataSourceInit.getJdbcTemplate().execute(sqlCommand.toString());

        return gamesToAdd;
    }

    public List<Game> retrieveAll() {

        List<Game> retrievedGames = new ArrayList<>();

        List<Game> results = (List<Game>) dataSourceInit.getJdbcTemplate().query(
                "SELECT * FROM game"
                , new Object[] { "aGame" },
                (RowMapper<Game>) (rs, rowNum) -> new Game(rs.getInt("id"), rs.getString("name"),
                        BigDecimal.valueOf(rs.getDouble("price"))));

        Arrays.asList(results).forEach(g -> GameTierra.logger.info(g.toString()));
        return retrievedGames;
    }

    @Override
    public Optional<Game> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public List<Game> findAll() {
        return null;
    }

    @Override
    public Game createOne(Game item) {
        return null;
    }
}

