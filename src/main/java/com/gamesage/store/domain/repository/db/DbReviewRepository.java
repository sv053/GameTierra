package com.gamesage.store.domain.repository.db;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.GameReview;
import com.gamesage.store.domain.model.Review;
import com.gamesage.store.domain.repository.ReviewRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
public class DbReviewRepository implements ReviewRepository<Review, Integer> {

    private static final String INSERT_REVIEW = "INSERT INTO review (user_id, game_id, order_datetime) " +
            "VALUES (?, ?, ?) ";
    private static final String SELECT_REVIEWS_BY_USER_QUERY =
            "SELECT review.id AS id, game_id, review_datetime " +
                    " FROM review " +
                    " WHERE user_id = ?";
    private static final String SELECT_REVIEWS_BY_GAME_QUERY =
            "SELECT review.id AS id, user_id, review_datetime, (AVG)mark as average_mark, opinion " +
                    " FROM review " +
                    " LEFT JOIN game " +
                    " ON game.id = review.game_id  " +
                    " WHERE game_id = ? " +
                    " group by id, user_id, review_datetime, opinion";
    private static final String SELECT_REVIEWS_QUERY =
            "SELECT review.id AS id, user_id, game_id, order_datetime, " +
                    " FROM review ";
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Review> reviewRowMapper;
    private final RowMapper<GameReview> gameReviewRowMapper;


    public DbReviewRepository(JdbcTemplate jdbcTemplate, RowMapper<Review> reviewRowMapper,
                              RowMapper<GameReview> gameReviewRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.reviewRowMapper = reviewRowMapper;
        this.gameReviewRowMapper = gameReviewRowMapper;
    }

    @Override
    public Review createOne(Review review) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con
                    .prepareStatement(INSERT_REVIEW,
                            Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, review.getUserId());
            ps.setInt(2, review.getGameId());
            ps.setTimestamp(3, Timestamp.valueOf(review.getDateTime()));
            return ps;
        }, keyHolder);
        return new Review(keyHolder.getKeyAs(Integer.class),
                review.getGameId(),
                review.getUserId(),
                review.getDateTime());
    }

    @Override
    public Optional<Review> findById(Integer id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    SELECT_REVIEWS_QUERY
                    , reviewRowMapper
                    , id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Review> findByUserId(Integer id) {
        return jdbcTemplate.query(SELECT_REVIEWS_BY_USER_QUERY, reviewRowMapper);
    }

    @Override
    public GameReview findByGameId(Integer id) {
        return jdbcTemplate.query(SELECT_REVIEWS_BY_GAME_QUERY, gameReviewRowMapper, id);
    }

    @Override
    public List<Review> findAll() {
        return jdbcTemplate.query(SELECT_REVIEWS_QUERY, rowMapper);
    }

    @Component
    static class ReviewRowMapper implements RowMapper<Review> {

        @Override
        public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
            Timestamp timestamp = rs.getTimestamp("review_datetime");
            LocalDateTime dateTime = timestamp.toLocalDateTime();

            return new Review(
                    rs.getInt("id"),
                    rs.getInt("userId"),
                    rs.getInt("gameId"),
                    dateTime);
        }
    }

    @Component
    static class GameReviewRowMapper implements RowMapper<GameReview> {

        @Override
        public GameReview mapRow(ResultSet rs, int rowNum) throws SQLException {
            Game game = new Game(
                    rs.getInt("game_id"),
                    rs.getString("name"),
                    rs.getBigDecimal("price"));

            Timestamp timestamp = rs.getTimestamp("order_datetime");
            LocalDateTime dateTime = timestamp.toLocalDateTime();

            return new GameReview(
                    rs.getInt("gameId"),
                    rs.getInt("id"),
                    rs.getDouble(""),
                    rs.getDouble("average_mark"));
        }
    }
}

