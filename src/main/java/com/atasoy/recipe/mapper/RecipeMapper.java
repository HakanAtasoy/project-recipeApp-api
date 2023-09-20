package com.atasoy.recipe.mapper;

import com.atasoy.recipe.entity.Recipe;
import com.atasoy.recipe.entity.enums.PreparationTime;
import com.atasoy.recipe.entity.enums.ServingSize;
import com.atasoy.recipe.model.RecipeModel;

import java.util.Date;

public class RecipeMapper {
    public static RecipeModel toRecipeModel(Recipe recipe) {

        RecipeModel recipeModel = new RecipeModel();
        recipeModel.setName(recipe.getName());
        recipeModel.setId(recipe.getId());
        recipeModel.setDescription(recipe.getDescription());
        recipeModel.setCategoryName(recipe.getCategory().getName());
        recipeModel.setUserName(recipe.getUser().getUserName());
        recipeModel.setStatus(1);
        recipeModel.setImageData(recipe.getImageData());
        recipeModel.setServingSize(recipe.getServingSize().getDisplayName());
        recipeModel.setPreparationTime(recipe.getPreparationTime().getDisplayName());
        return recipeModel;
    }

    public static Recipe toRecipe(RecipeModel recipeModel) {
        Recipe recipe = new Recipe();
        recipe.setDescription(recipeModel.getDescription());
        recipe.setCategoryId(recipeModel.getCategoryId());
        recipe.setUserId(recipeModel.getUserId());
        recipe.setName(recipeModel.getName());
        recipe.setId(recipeModel.getId());
        recipe.setStatus(1);
        recipe.setImageData(recipeModel.getImageData());
        recipe.setServingSize(ServingSize.fromDisplayName(recipeModel.getServingSize()));
        recipe.setPreparationTime(PreparationTime.fromDisplayTime(recipeModel.getPreparationTime()));
        recipe.setDeleted(false);
        recipe.setCreateDate(new Date());
        recipe.setUpdateDate(new Date());
        recipe.setDeleteDate(null);
        return recipe;
    }

}
