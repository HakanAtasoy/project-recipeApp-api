package com.atasoy.recipe.service.impl;

import com.atasoy.recipe.dao.RecipePageResponse;
import com.atasoy.recipe.entity.Category;
import com.atasoy.recipe.entity.Recipe;
import com.atasoy.recipe.mapper.RecipeMapper;
import com.atasoy.recipe.model.RecipeDetailModel;
import com.atasoy.recipe.model.RecipeModel;
import com.atasoy.recipe.repo.CategoryRepository;
import com.atasoy.recipe.repo.FavoriteRepository;
import com.atasoy.recipe.repo.RecipeRepository;
import com.atasoy.recipe.service.FavoriteService;
import com.atasoy.recipe.service.RecipeService;
import com.atasoy.recipe.service.ReviewService;
import com.atasoy.recipe.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final UserService userService;
    private final FavoriteService favoriteService;
    private final ReviewService reviewService;


    public RecipeServiceImpl(RecipeRepository recipeRepository, CategoryRepository categoryRepository, UserService userService, FavoriteService favoriteService, ReviewService reviewService, FavoriteRepository favoriteRepository) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.userService = userService;
        this.favoriteService = favoriteService;
        this.reviewService = reviewService;
    }

    @Transactional
    public RecipePageResponse getRecipesByCategoryIdAndSearchQuery(Integer categoryId, String searchQuery, Integer page, Integer pageSize) {

        Pageable pageable = PageRequest.of(page - 1, pageSize); // Page numbers start from 0
        Page<Recipe> recipePage;

        if (categoryId != 0 && searchQuery != null) {
            recipePage = recipeRepository.findByCategoryIdAndNameContainingIgnoreCaseAndDeletedFalseOrderByCreateDateAsc(categoryId, searchQuery, pageable);
        } else if (categoryId != 0) {
            recipePage = recipeRepository.findByCategoryIdAndDeletedFalseOrderByCreateDateAsc(categoryId, pageable);
        } else if (searchQuery != null) {
            recipePage = recipeRepository.findByNameContainingIgnoreCaseAndDeletedFalseOrderByCreateDateAsc(searchQuery, pageable);
        } else {
            recipePage = recipeRepository.findByDeletedFalseOrderByCreateDateAsc(pageable);
        }

        RecipePageResponse response = new RecipePageResponse();
        response.setContent(recipePage.getContent().stream()
                .map(RecipeMapper::toRecipeModel) // Use your mapper to convert Recipe to RecipeModel
                .collect(Collectors.toList()));
        response.setPage(recipePage.getNumber() + 1); // Page numbers start from 0, so add 1
        response.setTotalPages(recipePage.getTotalPages());
        response.setTotalItems(recipePage.getTotalElements());
        response.setPageSize(pageSize);

        return response;
    }

    @Transactional
    public List<RecipeModel> getRandomRecipes() {
        // Fetch a larger set of recipes (e.g., 100)
        List<Recipe> allRecipes = recipeRepository.findAllByDeletedFalse();

        // Shuffle the list to randomize the order
        Collections.shuffle(allRecipes);

        // Get the first 6 recipes from the shuffled list
        List<Recipe> randomRecipes = allRecipes.subList(0, Math.min(6, allRecipes.size()));

        // Map the random recipes to RecipeModel
        return randomRecipes.stream()
                .map(RecipeMapper::toRecipeModel)
                .collect(Collectors.toList());
    }


    @Transactional
    public RecipeModel getRecipe(Integer recipeId) {
        Recipe recipe = recipeRepository.findByIdAndDeletedFalse(recipeId);

        if (recipe != null) {
            return RecipeMapper.toRecipeModel(recipe);
        } else {
            // Handle the case where the recipe doesn't exist
            return null; // or throw an exception, return a default model, etc.
        }
    }

    @Transactional
    public ResponseEntity<RecipeDetailModel> getRecipeDetail(Integer recipeId) {

        RecipeDetailModel recipeDetailModel = new RecipeDetailModel();
        recipeDetailModel.setRecipeModel(this.getRecipe(recipeId));

        Integer userId = userService.getUserFromSession().getId();
        recipeDetailModel.setInUsersFavorites(favoriteService.checkIfLiked(userId, recipeId));

        recipeDetailModel.setLikeCount(favoriteService.numberOfLikes(recipeId));

        recipeDetailModel.setReviews(reviewService.getAllReviews(recipeId));

        return ResponseEntity.ok(recipeDetailModel);

    }


    @Transactional
    public ResponseEntity<RecipePageResponse> getUserRecipes(Integer page, Integer pageSize) {
        Integer userId = userService.getUserFromSession().getId();
        Pageable pageable = PageRequest.of(page - 1, pageSize); // Page numbers start from 0
        Page<Recipe> recipePage = recipeRepository.findByUserIdAndDeletedFalseOrderByCreateDateAsc(userId, pageable);
        RecipePageResponse response = new RecipePageResponse();
        response.setContent(recipePage.getContent().stream()
                .map(RecipeMapper::toRecipeModel)
                .collect(Collectors.toList()));
        response.setPage(recipePage.getNumber() + 1);
        response.setTotalPages(recipePage.getTotalPages());
        response.setTotalItems(recipePage.getTotalElements());
        response.setPageSize(pageSize);
        return ResponseEntity.ok(response);

    }

    @Transactional
    public void addRecipe(RecipeModel recipeModel, MultipartFile imageFile) throws IOException {
        // Create a new Recipe instance and set its properties from the RecipeModel

        Integer userId = userService.getUserFromSession().getId();

        if (imageFile != null && !imageFile.isEmpty()) {
            byte[] imageBytes = imageFile.getBytes();
            recipeModel.setImageData(imageBytes);
        } else {
            recipeModel.setImageData(new byte[0]); // Set image data to an empty byte array
        }
        recipeModel.setUserId(userId);
        Category category = categoryRepository.findByNameAndDeletedFalse(recipeModel.getCategoryName());
        recipeModel.setCategoryId(category.getId());
        recipeRepository.save(RecipeMapper.toRecipe(recipeModel));

    }


    public boolean updateRecipe(RecipeModel recipeModel, MultipartFile imageFile) {
        Recipe oldRecipe = recipeRepository.findById(recipeModel.getId()).orElse(null);

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        String username = userDetails.getUsername();

        if (oldRecipe == null || oldRecipe.isDeleted()
                || !Objects.equals(oldRecipe.getUser().getUsername(), username)) {
            return false;
        }


        oldRecipe.setUpdateDate(new Date());
        oldRecipe.setName(recipeModel.getName());
        oldRecipe.setCategoryId(recipeModel.getCategoryId());
        oldRecipe.setDeleted(recipeModel.isDeleted());
        oldRecipe.setDescription(recipeModel.getDescription());
        oldRecipe.setImageData(recipeModel.getImageData());

        recipeRepository.save(oldRecipe);

        return true;
    }

    @Transactional
    public boolean deleteRecipe(Integer recipeId) {
        Recipe oldRecipe = recipeRepository.findById(recipeId).orElse(null);

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        String username = userDetails.getUsername();

        if (oldRecipe == null || !Objects.equals(oldRecipe.getUser().getUsername(), username))
            return false;

        oldRecipe.setDeleted(true);
        oldRecipe.setDeleteDate(new Date());

        recipeRepository.save(oldRecipe);
        return true;
    }
}
