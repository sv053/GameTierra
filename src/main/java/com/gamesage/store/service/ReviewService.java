package com.gamesage.store.service;

import com.gamesage.store.domain.model.GameReview;
import com.gamesage.store.domain.model.Review;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.ReviewRepository;
import com.gamesage.store.exception.CannotCreateEntityException;
import com.gamesage.store.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository<Review, Integer> repository;
    private final UserService userService;
    private final GameRatingService ratingService;

    public ReviewService(ReviewRepository<Review, Integer> repository,
                         UserService userService, GameRatingService ratingService) {
        this.repository = repository;
        this.userService = userService;
        this.ratingService = ratingService;
    }

    public Review findById(int id) throws Throwable {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
    }

    public List<Review> findByUserId(int id, int page, int size) {
        List<Review> foundReviews = repository.findByUserId(id, page, size);
        return checkReviewsListNull(id, foundReviews);
    }

    public GameReview findByGameId(int id, int page, int size) {
        return repository.findByGameId(id, page, size);
    }

    private List<Review> checkReviewsListNull(int id, List<Review> foundReviews) {
        if (foundReviews.isEmpty()) {
            throw new EntityNotFoundException(id, Review.class.getSimpleName());
        }
        return foundReviews;
    }

    public Review createReview(Review review) {
        User user = userService.findById(review.getUserId());
        Integer gameId = review.getGameId();
        if (!user.hasGame(gameId)) {
            throw new CannotCreateEntityException("User is not an owner");
        }
        ratingService.addRating(gameId, review.getRating());
        return repository.createOne(review);
    }

    public Review updateReview(Review review) throws Throwable {
        Review existedReview = findById(review.getId());
        Integer gameId = review.getGameId();
        if (!(existedReview.getGameId().equals(review.getGameId())
                && existedReview.getUserId().equals(review.getUserId()))) {
            throw new EntityNotFoundException(review.getId(), Review.class.getSimpleName());
        }
        ratingService.updateRating(gameId, review.getRating());
        return repository.updateReview(review);
    }
}

