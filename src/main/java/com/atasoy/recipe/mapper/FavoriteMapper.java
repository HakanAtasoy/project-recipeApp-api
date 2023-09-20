package com.atasoy.recipe.mapper;

import com.atasoy.recipe.entity.Favorite;
import com.atasoy.recipe.model.FavoriteModel;

import java.util.Date;

import static com.atasoy.recipe.mapper.RecipeMapper.toRecipeModel;


public class FavoriteMapper {
    public static FavoriteModel toFavoriteModel(Favorite favorite) {
        FavoriteModel favoriteModel = new FavoriteModel();
        favoriteModel.setRecipeModel(toRecipeModel(favorite.getRecipe()));
        favoriteModel.setUserName(favorite.getUser().getUserName());
        favoriteModel.setUserId(favorite.getUserId());
        favoriteModel.setId(favorite.getId());
        return favoriteModel;
    }

    public static Favorite toFavorite(FavoriteModel favoriteModel) {
        Favorite favorite = new Favorite();
        favorite.setUserId(favoriteModel.getUserId());
        favorite.setRecipeId(favoriteModel.getRecipeModel().getId());
        favorite.setDeleted(false);
        favorite.setCreateDate(new Date());
        favorite.setUpdateDate(new Date());
        favorite.setDeleteDate(null);
        return favorite;
    }
}
