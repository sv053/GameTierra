package com.gamesage.store.domain.repository.db;

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

    private static final String INSERT_REVIEW = "INSERT INTO review (user_id, game_id, rating, opinion, review_datetime) " +
            "VALUES (?, ?, ?, ?, ?) ";
    private static final String SELECT_REVIEWS_BY_USER_QUERY =
            "SELECT id, game_id, user_id, rating, opinion, review_datetime " +
                    " FROM review " +
                    " WHERE user_id = ?";
    private static final String SELECT_REVIEWS_BY_GAME_QUERY =
            "SELECT id, user_id, review_datetime, (AVG)rating as average_rating, opinion " +
                    " FROM review " +
                    " WHERE game_id = ? " +
                    " group by id, user_id, review_datetime, opinion";
    private static final String SELECT_REVIEW_QUERY =
            "SELECT id, user_id, game_id, rating, opinion, review_datetime " +
                    " FROM review " +
                    " WHERE id = ? ";
    private static final String SELECT_REVIEWS_QUERY =
            "SELECT id, user_id, game_id, rating, opinion, review_datetime " +
                    " FROM review ";
    private static final String UPDATE_REVIEW_QUERY =
            "UPDATE review SET rating = ?, opinion = ? " +
                    "WHERE id = ?";
    private static final String REMOVE_REVIEWS = "DELETE " +
            " FROM review ";
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Review> reviewRowMapper;


    public DbReviewRepository(JdbcTemplate jdbcTemplate, RowMapper<Review> reviewRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.reviewRowMapper = reviewRowMapper;
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
            ps.setInt(3, review.getRating());
            ps.setString(4, review.getOpinion());
            ps.setTimestamp(5, Timestamp.valueOf(review.getDateTime()));
            return ps;
        }, keyHolder);
        return new Review(keyHolder.getKeyAs(Integer.class),
                review.getGameId(),
                review.getUserId(),
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
    public List<Review> findByUserId(Integer id) {
        return jdbcTemplate.query(SELECT_REVIEWS_BY_USER_QUERY, reviewRowMapper);
    }

    @Override
    public List<Review> findByGameId(Integer id) {
        return jdbcTemplate.query(SELECT_REVIEWS_BY_GAME_QUERY, reviewRowMapper, id);
    }

    @Override
    public List<Review> findAll() {
        return jdbcTemplate.query(SELECT_REVIEWS_QUERY, reviewRowMapper);
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update(REMOVE_REVIEWS);
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
                    rs.getInt("rating"),
                    rs.getString("opinion"),
                    dateTime
            );
        }
    }
}

