package com.atasoy.recipe.controller;

import com.atasoy.recipe.dao.RecipePageResponse;
import com.atasoy.recipe.model.RecipeDetailModel;
import com.atasoy.recipe.model.RecipeModel;
import com.atasoy.recipe.service.RecipeService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:4200") // Specify allowed origin(s)
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/query")
    public ResponseEntity<RecipePageResponse> getRecipes(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String searchQuery,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "12") Integer pageSize) {

        RecipePageResponse recipePage = recipeService.getRecipesByCategoryIdAndSearchQuery(categoryId, searchQuery, page, pageSize);
        return ResponseEntity.ok(recipePage);
    }

    @GetMapping("/main")
    public List<RecipeModel> mainPageRecipes() {
        return recipeService.getRandomRecipes();
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<RecipeDetailModel> getRecipeDetail(@PathVariable("id") Integer recipeId) {
        return recipeService.getRecipeDetail(recipeId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public RecipeModel getRecipe(@PathVariable("id") Integer recipeId) {
        return recipeService.getRecipe(recipeId);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<RecipePageResponse> getUserRecipes(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "12") Integer pageSize) {
        return recipeService.getUserRecipes(page, pageSize);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public void addRecipe(@Valid @RequestPart RecipeModel recipeModel,
                          @RequestPart("imageFile") MultipartFile imageFile) throws IOException {
        recipeService.addRecipe(recipeModel, imageFile);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public boolean updateRecipe(@Valid @RequestPart RecipeModel recipeModel,
                                @RequestPart("imageFile") MultipartFile imageFile) {
        return recipeService.updateRecipe(recipeModel, imageFile);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public boolean deleteRecipe(@PathVariable("id") Integer recipeId) {
        return recipeService.deleteRecipe(recipeId);
    }
}
