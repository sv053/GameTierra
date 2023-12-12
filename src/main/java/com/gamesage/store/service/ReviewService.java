package com.gamesage.store.service;

import com.gamesage.store.domain.model.GameReview;
import com.gamesage.store.domain.model.Review;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.ReviewRepository;
import com.gamesage.store.exception.CannotCreateEntityException;
import com.gamesage.store.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository<Review, Integer> repository;
    private final UserService userService;

    public ReviewService(ReviewRepository<Review, Integer> repository,
                         UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public Review findById(int id) throws Throwable {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
    }

    public List<Review> findByUserId(int id, int page, int size) {
        List<Review> foundReviews = repository.findByUserId(id);
        if (foundReviews.isEmpty()) {
            throw new EntityNotFoundException(id, Review.class.getSimpleName());
        }
        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, foundReviews.size());
        return foundReviews.subList(fromIndex, toIndex);
    }

    public List<Review> findByGameId(int id, int page, int size) {
        List<Review> foundReviews = repository.findByGameId(id);
        if (foundReviews.isEmpty()) {
            throw new EntityNotFoundException(id, Review.class.getSimpleName());
        }
        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, foundReviews.size());
        return foundReviews.subList(fromIndex, toIndex);
    }

    public GameReview createGameReview(List<Review> reviews, int gameId) {
        return new GameReview(gameId, reviews).setAvgRating();
    }

    public GameReview prepareGameReview(int gameId, int page, int size) {
        return createGameReview(findByGameId(gameId, page, size), gameId);
    }

    public List<Review> findAll(int page, int size) {
        List<Review> foundReviews = repository.findAll();
        if (foundReviews.isEmpty()) {
            throw new EntityNotFoundException(Review.class.getSimpleName());
        }
        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, foundReviews.size());
        return foundReviews.subList(fromIndex, toIndex);
    }

    private boolean existsReview(Review review) {
        return repository.findById(review.getId()).isPresent();
    }


    public Review updateOrCreateReview(Review review) throws Throwable {
        if (existsReview(review)) {
            return updateReview(review);
        } else {
            return createReview(review);
        }
    }

    @Transactional
    public Review createReview(Review review) {
        User user = userService.findById(review.getUserId());
        if (user.hasGame(review.getGameId())) {
            return repository.createOne(review);
        } else throw new CannotCreateEntityException("User is not an owner");
    }

    public Review updateReview(Review review) throws Throwable {
        Review existedReview = findById(review.getId());
        if (existedReview.getGameId().equals(review.getGameId())
                && existedReview.getUserId().equals(review.getUserId())) {
            return repository.updateReview(review);
        } else throw new CannotCreateEntityException();
    }
}

