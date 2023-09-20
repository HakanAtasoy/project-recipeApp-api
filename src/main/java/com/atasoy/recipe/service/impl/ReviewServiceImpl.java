package com.atasoy.recipe.service.impl;

import com.atasoy.recipe.entity.Review;
import com.atasoy.recipe.mapper.ReviewMapper;
import com.atasoy.recipe.model.ReviewModel;
import com.atasoy.recipe.repo.ReviewRepository;
import com.atasoy.recipe.service.ReviewService;
import com.atasoy.recipe.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;

    public ReviewServiceImpl(ReviewRepository reviewRepository, UserService userService) {
        this.reviewRepository = reviewRepository;
        this.userService = userService;
    }

    public List<ReviewModel> getAllReviews(Integer recipeId) {
        return reviewRepository.findAll().stream().filter(r -> !r.isDeleted() && Objects.equals(r.getRecipeId(), recipeId)).map(ReviewMapper::toReviewModel).toList();
    }

    public Optional<ReviewModel> getReview(Integer reviewId) {
        return reviewRepository.findById(reviewId).filter(r -> !r.isDeleted()).map(ReviewMapper::toReviewModel);
    }

    public ResponseEntity<ReviewModel> addReview(Integer recipeId, String description) {
        Review review = new Review();
        review.setUser(userService.getUserFromSession());
        review.setUserId(userService.getUserFromSession().getId());
        review.setRecipeId(recipeId);
        review.setDescription(description);
        review.setCreateDate(new Date());
        reviewRepository.save(review);
        return ResponseEntity.ok(ReviewMapper.toReviewModel(review));
    }

    public boolean updateReview(ReviewModel reviewModel) {
        Review oldReview = reviewRepository.findById(reviewModel.getId()).orElse(null);

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        String username = userDetails.getUsername();

        if (oldReview == null || oldReview.isDeleted()
                || !Objects.equals(oldReview.getUser().getUsername(), username)) {
            return false;
        }

        oldReview.setUpdateDate(new Date());
        oldReview.setRecipeId(reviewModel.getRecipeId());
        oldReview.setRating(reviewModel.getRating());
        oldReview.setDeleted(reviewModel.isDeleted());
        oldReview.setDescription(reviewModel.getDescription());
        oldReview.setUserId(reviewModel.getUserId());

        reviewRepository.save(oldReview);

        return true;
    }

    public boolean deleteReview(Integer reviewId) {
        Review oldReview = reviewRepository.findById(reviewId).orElse(null);

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        String username = userDetails.getUsername();

        if (oldReview == null || !Objects.equals(oldReview.getUser().getUsername(), username))
            return false;

        oldReview.setDeleted(true);
        oldReview.setDeleteDate(new Date());

        reviewRepository.save(oldReview);
        return true;
    }
}
