package com.atasoy.recipe.controller;

import com.atasoy.recipe.model.ReviewModel;
import com.atasoy.recipe.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public List<ReviewModel> getAllReviews(Integer recipeId) {
        return reviewService.getAllReviews(recipeId);
    }

    @GetMapping("/{id}")
    public Optional<ReviewModel> getReview(@PathVariable("id") Integer reviewId) {
        return reviewService.getReview(reviewId);
    }

    @PostMapping
    public ResponseEntity<ReviewModel> addReview(@RequestParam Integer recipeId, @RequestParam String description) {
        return reviewService.addReview(recipeId, description);
    }

    @PutMapping
    public boolean updateReview(@Valid @RequestBody ReviewModel reviewModel) {
        return reviewService.updateReview(reviewModel);
    }

    @DeleteMapping("/{id}")
    public boolean deleteReview(@PathVariable("id") Integer reviewId) {
        return reviewService.deleteReview(reviewId);
    }
}
