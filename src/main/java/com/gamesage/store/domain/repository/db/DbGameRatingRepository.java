package com.gamesage.store.domain.repository.db;

import com.gamesage.store.domain.repository.RatingRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class DbGameRatingRepository implements RatingRepository {

    private static final String SELECT_RATING_QUERY =
            " SELECT rating " +
                    "FROM game_rating " +
                    "WHERE game_id = ? ";

    private static final String SELECT_RATINGS_AMOUNT_QUERY =
            " SELECT ratings_amount " +
                    "FROM game_rating " +
                    "WHERE game_id = ? ";

    private static final String INSERT_NEW_GAME_QUERY =
            "INSERT INTO game_rating (game_id, rating, ratings_amount) " +
                    "VALUES (?, ?, ?) ";

    private static final String UPDATE_RATING_QUERY = "" +
            "UPDATE game_rating " +
            "SET rating = rating + ? " +
            "WHERE game_id = ?";

    private static final String ADD_RATING_QUERY = "" +
            "UPDATE game_rating " +
            "SET rating = rating + ? , ratings_amount = ratings_amount + 1 " +
            "WHERE game_id = ?";
    private final JdbcTemplate jdbcTemplate;

    public DbGameRatingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer addGame(Integer gameId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con
                    .prepareStatement(INSERT_NEW_GAME_QUERY,
                            Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, gameId);
            ps.setInt(2, 0);
            ps.setInt(3, 0);
            return ps;
        }, keyHolder);
        return keyHolder.getKeyAs(Integer.class);
    }

    @Override
    public Integer findRating(Integer gameId) {
        try {
            return jdbcTemplate.queryForObject(
                    SELECT_RATING_QUERY
                    , Integer.class
                    , gameId);
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

    @Override
    public Integer findRatingsAmount(Integer gameId) {
        try {
            return jdbcTemplate.queryForObject(
                    SELECT_RATINGS_AMOUNT_QUERY
                    , Integer.class
                    , gameId);
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

    @Override
    public Integer addRating(Integer gameId, Boolean ratingToCount) {
        return jdbcTemplate.update(ADD_RATING_QUERY
                , ratingToCount ? 1 : 0
                , gameId
        );
    }

    @Override
    public Integer updateRating(Integer gameId, Boolean ratingToCount) {
        return jdbcTemplate.update(UPDATE_RATING_QUERY
                , ratingToCount ? 1 : 0
                , gameId
        );
    }
}

