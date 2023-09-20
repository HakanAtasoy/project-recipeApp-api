package com.atasoy.recipe.repo;

import com.atasoy.recipe.entity.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    Optional<Favorite> findByIdAndDeletedFalse(Integer favoriteId);

    Integer countByRecipeIdAndDeletedFalse(Integer recipeId);

    Page<Favorite> findByUserIdAndDeletedFalseOrderByCreateDateAsc(Integer userId, Pageable pageable);

    Optional<Favorite> findByUserIdAndRecipeIdAndDeletedFalse(Integer userId, Integer recipeId);
}
