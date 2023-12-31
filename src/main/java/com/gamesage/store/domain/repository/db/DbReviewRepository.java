package com.gamesage.store.domain.repository.db;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
public class DbReviewRepository implements ReviewRepository<Review, Integer> {

    private static final String INSERT_REVIEW =
            "INSERT INTO review (user_id, game_id, rating, opinion, review_datetime) " +
                    "VALUES (?, ?, ?, ?, ?) ";
    private static final String SELECT_REVIEWS_RANGE_BY_USER_QUERY =
            "SELECT id, user_id, game_id, rating, opinion, review_datetime " +
                    " FROM review " +
                    " WHERE user_id = ? " +
                    " LIMIT ? " +
                    " OFFSET ? ";
    private static final String SELECT_REVIEWS_RANGE_BY_GAME_QUERY =
            " SELECT r1.game_id, r2.id, r2.user_id, r2.rating, r2.opinion, r2.review_datetime, " +
                    "(SELECT AVG(CAST(rating AS DOUBLE)) FROM review WHERE game_id = r1.game_id) AS overall_average_rating, " +
                    "(SELECT SUM(CASE WHEN rating THEN 1 ELSE 0 END) FROM review WHERE game_id = r1.game_id) AS overall_positive_rating, " +
                    "(SELECT SUM(CASE WHEN NOT rating THEN 1 ELSE 0 END) FROM review WHERE game_id = r1.game_id) AS overall_negative_rating " +
                    "FROM review r1 " +
                    "INNER JOIN review r2 ON r1.game_id = r2.game_id " +
                    "WHERE r1.game_id = ? " +
                    "GROUP BY r1.game_id, r2.id, r2.user_id, r2.rating, r2.opinion, r2.review_datetime " +
                    "LIMIT ? OFFSET ?";

    private static final String SELECT_REVIEW_QUERY =
            "SELECT id, user_id, game_id, rating, opinion, review_datetime " +
                    " FROM review " +
                    " WHERE id = ? ";
    private static final String UPDATE_REVIEW_QUERY =
            "UPDATE review SET rating = ?, opinion = ? " +
                    "WHERE id = ?";
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Review> reviewRowMapper;
    private final RowMapper<GameReview> gameReviewRowMapper;


    public DbReviewRepository(JdbcTemplate jdbcTemplate, RowMapper<Review> reviewRowMapper, RowMapper<GameReview> gameReviewRowMapper) {
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
            ps.setBoolean(3, review.getRating());
            ps.setString(4, review.getOpinion());
            ps.setTimestamp(5, Timestamp.valueOf(review.getDateTime()));
            return ps;
        }, keyHolder);
        return new Review(keyHolder.getKeyAs(Integer.class),
                review.getUserId(),
                review.getGameId(),
                review.getRating(),
                review.getOpinion(),
                review.getDateTime());
    }

    public Review updateReview(Review review) {
        jdbcTemplate.update(UPDATE_REVIEW_QUERY
                , review.getRating()
                , review.getOpinion()
                , review.getId());
        return review;
    }

    @Override
    public Optional<Review> findById(Integer id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    SELECT_REVIEW_QUERY
                    , reviewRowMapper
                    , id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Review> findByUserId(Integer id, Integer page, Integer size) {
        int startIndex = size * page - size;
        return jdbcTemplate.query(
                SELECT_REVIEWS_RANGE_BY_USER_QUERY,
                reviewRowMapper,
                id, size, startIndex);
    }

    @Override
    public GameReview findByGameId(Integer id, Integer page, Integer size) {
        int startIndex = size * page - size;
        return jdbcTemplate.queryForObject(SELECT_REVIEWS_RANGE_BY_GAME_QUERY, gameReviewRowMapper, id, size, startIndex);
    }

    @Component
    static class ReviewRowMapper implements RowMapper<Review> {

        @Override
        public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
            Timestamp timestamp = rs.getTimestamp("review_datetime");
            LocalDateTime dateTime = timestamp.toLocalDateTime();

            return new Review(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getInt("game_id"),
                    rs.getBoolean("rating"),
                    rs.getString("opinion"),
                    dateTime
            );
        }

        @Component
        static class GameReviewRowMapper implements RowMapper<GameReview> {

            @Override
            public GameReview mapRow(ResultSet rs, int rowNum) throws SQLException {
                int gameId = rs.getInt("game_id");
                Double avgRating = rs.getDouble("overall_average_rating");
                int positiveRating = rs.getInt("overall_positive_rating");
                int negativeRating = rs.getInt("overall_negative_rating");

                List<Review> reviews = new ArrayList<>();
                do {
                    Timestamp timestamp = rs.getTimestamp("review_datetime");
                    LocalDateTime dateTime = timestamp.toLocalDateTime();

                    reviews.add(new Review(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            gameId,
                            rs.getBoolean("rating"),
                            rs.getString("opinion"),
                            dateTime));
                }
                while (rs.next() && rs.getInt("game_id") == gameId);
                return new GameReview(gameId, reviews, avgRating, positiveRating, negativeRating);
            }
        }
    }
}

