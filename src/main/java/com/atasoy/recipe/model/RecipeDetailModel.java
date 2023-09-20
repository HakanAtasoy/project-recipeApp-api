package com.atasoy.recipe.model;

import java.util.List;

public class RecipeDetailModel {

    RecipeModel recipeModel;

    Integer likeCount;

    Boolean inUsersFavorites;

    List<ReviewModel> reviews;

    public RecipeModel getRecipeModel() {
        return recipeModel;
    }

    public void setRecipeModel(RecipeModel recipeModel) {
        this.recipeModel = recipeModel;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Boolean getInUsersFavorites() {
        return inUsersFavorites;
    }

    public void setInUsersFavorites(Boolean inUsersFavorites) {
        this.inUsersFavorites = inUsersFavorites;
    }

    public List<ReviewModel> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewModel> reviews) {
        this.reviews = reviews;
    }
}
