package com.atasoy.recipe.mapper;

import com.atasoy.recipe.entity.Review;
import com.atasoy.recipe.model.ReviewModel;

import java.util.Date;

public class ReviewMapper {
    public static ReviewModel toReviewModel(Review review) {
        ReviewModel reviewModel = new ReviewModel();
        reviewModel.setDescription(review.getDescription());
        //reviewModel.setRating(review.getRating());
        reviewModel.setUserName(review.getUser().getUserName());
        reviewModel.setCreateDate(review.getCreateDate());
        reviewModel.setRecipeId(review.getRecipeId());
        return reviewModel;
    }

    public static Review toReview(ReviewModel reviewModel) {
        Review review = new Review();
        review.setDescription(reviewModel.getDescription());
        //review.setRating(reviewModel.getRating());
        review.setUserId(reviewModel.getUserId());
        review.setRecipeId(reviewModel.getRecipeId());
        review.setDeleted(false);
        review.setCreateDate(new Date());
        review.setUpdateDate(new Date());
        review.setDeleteDate(null);
        return review;
    }
}
