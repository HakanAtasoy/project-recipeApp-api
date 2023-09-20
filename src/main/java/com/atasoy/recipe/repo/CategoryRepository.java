package com.atasoy.recipe.repo;

import com.atasoy.recipe.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Page<Category> findByDeletedFalseOrderByCreateDateAsc(Pageable pageable);

    Category findByNameAndDeletedFalse(String categoryName);

}
