package com.gamesage.store.domain.repository.db;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.repository.GameRepository;
import com.gamesage.store.util.RandomBigDecimal;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DbGameRepository extends GameRepository {

    @Override
    public List<Game> create(List<Game> gamesToAdd) {
        try (final Connection connection = DbConnector.getConnection("jdbc:sqlite:gamesage.db")) {
            Class.forName("org.sqlite.JDBC");

            String sqlCommand = "INSERT INTO game (id, name, price) \n" +
                    "VALUES (1, \"GRAND_THEFT_AUTO\", 32.2),\n" +
                    "(2, \"FORZA_HORIZON\", 71.73),\n" +
                    "(3, \"ASSASSIN_S_CREED\", 29.83),\n" +
                    "(4, \"DOOM_ETERNAL\", 43.87),\n" +
                    "(5, \"RED_DEAD_REDEMPTION\", 98.3),\n" +
                    "(6, \"THE_WITCHER\", 18.7),\n" +
                    "(7, \"THE_LAST_OF_US\", 158.3)";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlCommand);
        } catch (SQLException | ClassNotFoundException e) {
        }
        return List.of(
                new Game("THE_WITCHER", BigDecimal.valueOf(17.28d)),
                new Game("GRAND_THEFT_AUTO", RandomBigDecimal.getAndFormatRandomBigDecimal()),
                new Game("RED_DEAD_REDEMPTION", RandomBigDecimal.getAndFormatRandomBigDecimal()),
                new Game("SKYRIM", BigDecimal.valueOf(87.88d)),
                new Game("FORZA_HORIZON", RandomBigDecimal.getAndFormatRandomBigDecimal()),
                new Game("DOOM_ETERNAL", BigDecimal.valueOf(635.48d)),
                new Game("ASSASSIN_S_CREED", RandomBigDecimal.getAndFormatRandomBigDecimal()),
                new Game("A_NEW_ONE", RandomBigDecimal.getAndFormatRandomBigDecimal()));
    }

    @Override
    public List<Game> findAll() {

        List<Game> retrievedGames = new ArrayList<>();

        try (final Connection connection = DbConnector.getConnection("jdbc:sqlite:gamesage.db")) {
            Class.forName("org.sqlite.JDBC");

            String sqlCommand = "SELECT * FROM game";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlCommand);

            while (resultSet.next()) {
                retrievedGames.add(new Game(resultSet.getInt(1),
                        resultSet.getString(2),
                        BigDecimal.valueOf(resultSet.getDouble(3))));
            }
            super.create(retrievedGames);
        } catch (SQLException | ClassNotFoundException e) {
        }
        return retrievedGames;
    }
}

