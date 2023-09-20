package com.atasoy.recipe.repo;

import com.atasoy.recipe.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    Optional<Review> findByRecipeIdAndDeletedFalse(Integer recipeId);
}
