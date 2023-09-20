package com.atasoy.recipe.repo;

import com.atasoy.recipe.entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

    Page<Recipe> findByCategoryIdAndDeletedFalseOrderByCreateDateAsc(Integer categoryId, Pageable pageable);

    Page<Recipe> findByNameContainingIgnoreCaseAndDeletedFalseOrderByCreateDateAsc(String name, Pageable pageable);

    Page<Recipe> findByCategoryIdAndNameContainingIgnoreCaseAndDeletedFalseOrderByCreateDateAsc(Integer categoryId, String searchQuery, Pageable page);

    Page<Recipe> findByDeletedFalseOrderByCreateDateAsc(Pageable pageable);

    List<Recipe> findByDeletedFalseOrderByCreateDateAsc();

    Recipe findByIdAndDeletedFalse(Integer recipeId);

    int countByDeletedFalse();

    List<Recipe> findTop6RecipesByDeletedFalseOrderByCreateDateAsc();

    Page<Recipe> findByUserIdAndDeletedFalseOrderByCreateDateAsc(Integer userId, Pageable pageable);

    List<Recipe> findAllByDeletedFalse();
}
