package com.atasoy.recipe.service;

import com.atasoy.recipe.dao.RecipePageResponse;
import com.atasoy.recipe.model.RecipeDetailModel;
import com.atasoy.recipe.model.RecipeModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface RecipeService {
    public RecipeModel getRecipe(Integer recipeId);

    ResponseEntity<RecipeDetailModel> getRecipeDetail(Integer recipeId);

    ResponseEntity<RecipePageResponse> getUserRecipes(Integer page, Integer pageSize);

    public void addRecipe(RecipeModel recipeModel, MultipartFile imageFile) throws IOException;

    public boolean updateRecipe(RecipeModel recipeModel, MultipartFile imageFile);

    public boolean deleteRecipe(Integer recipeId);

    RecipePageResponse getRecipesByCategoryIdAndSearchQuery(Integer categoryId, String searchQuery, Integer page, Integer pageSize);

    List<RecipeModel> getRandomRecipes();
}