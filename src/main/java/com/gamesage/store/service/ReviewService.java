package com.gamesage.store.service;

import com.gamesage.store.domain.model.GameReview;
import com.gamesage.store.domain.model.Review;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.ReviewRepository;
import com.gamesage.store.exception.CannotCreateEntityException;
import com.gamesage.store.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository<Review, Integer> repository;
    private final UserService userService;
    private final GameReviewService ratingService;

    public ReviewService(ReviewRepository<Review, Integer> repository,
                         UserService userService, GameReviewService ratingService) {
        this.repository = repository;
        this.userService = userService;
        this.ratingService = ratingService;
    }

    public Review findById(int id) throws Throwable {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
    }

    public List<Review> findByUserId(int id, int page, int size) {
        return Optional.ofNullable(repository.findByUserId(id, page, size))
                .filter(list -> !list.isEmpty())
                .orElse(Collections.emptyList());
    }

    public GameReview findByGameId(int id, int page, int size) {
        return repository.findByGameId(id, page, size);
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

