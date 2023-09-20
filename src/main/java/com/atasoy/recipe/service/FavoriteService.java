package com.atasoy.recipe.service;

import com.atasoy.recipe.dao.FavoritePageResponse;
import com.atasoy.recipe.model.FavoriteModel;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface FavoriteService {
    public ResponseEntity<FavoritePageResponse> getAllFavorites(Integer page, Integer pageSize);

    public Optional<FavoriteModel> getFavorite(Integer favoriteId);

    public void addFavorite(Integer recipeId);

    public boolean deleteFavorite(Integer recipeId);

    public boolean removeFromFavorites(Integer favoriteId);

    public boolean checkIfLiked(Integer userId, Integer recipeId);

    public Integer numberOfLikes(Integer recipeId);
}
