package com.atasoy.recipe.service;

import com.atasoy.recipe.model.ReviewModel;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    public List<ReviewModel> getAllReviews(Integer recipeId);

    public Optional<ReviewModel> getReview(Integer reviewId);

    public ResponseEntity<ReviewModel> addReview(Integer recipeId, String description);

    public boolean updateReview(ReviewModel reviewModel);

    public boolean deleteReview(Integer reviewId);
}
