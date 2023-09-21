package com.atasoy.recipe.service.impl;

import com.atasoy.recipe.dao.CategoryPageResponse;
import com.atasoy.recipe.entity.Category;
import com.atasoy.recipe.entity.Recipe;
import com.atasoy.recipe.mapper.CategoryMapper;
import com.atasoy.recipe.model.CategoryModel;
import com.atasoy.recipe.repo.CategoryRepository;
import com.atasoy.recipe.repo.RecipeRepository;
import com.atasoy.recipe.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final RecipeRepository recipeRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, RecipeRepository recipeRepository) {
        this.categoryRepository = categoryRepository;
        this.recipeRepository = recipeRepository;
    }

    @Transactional
    public CategoryPageResponse getAllCategories(Integer page, Integer pageSize) {

        if (pageSize == null)
            pageSize = 12;
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Category> categoryPage = categoryRepository.findByDeletedFalseOrderByCreateDateAsc(pageable);
        CategoryPageResponse response = new CategoryPageResponse();
        response.setContent(categoryPage.getContent().stream()
                .map(CategoryMapper::toCategoryModel) // Use your mapper to convert Category to CategoryModel
                .collect(Collectors.toList()));
        response.setPage(categoryPage.getNumber() + 1); // Page numbers start from 0, so add 1
        response.setTotalPages(categoryPage.getTotalPages());
        response.setTotalItems(categoryPage.getTotalElements());
        response.setPageSize(12);

        return response;
    }

    @Transactional
    public Optional<CategoryModel> getCategory(Integer categoryId) {
        return categoryRepository.findById(categoryId).filter(c -> !c.isDeleted()).map(CategoryMapper::toCategoryModel);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void addCategory(CategoryModel categoryModel, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            byte[] imageBytes = imageFile.getBytes();
            categoryModel.setImageData(imageBytes);
        } else {
            categoryModel.setImageData(new byte[0]); // Set image data to an empty byte array
        }
        categoryRepository.save(CategoryMapper.toCategory(categoryModel));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public boolean updateCategory(CategoryModel categoryModel, MultipartFile imageFile) {
        Category oldCategory = categoryRepository.findById(categoryModel.getId()).orElse(null);

        if (oldCategory == null || oldCategory.isDeleted()) {
            return false;
        }

        oldCategory.setUpdateDate(new Date());
        oldCategory.setName(categoryModel.getName());
        oldCategory.setDeleted(categoryModel.isDeleted());
        oldCategory.setDescription(categoryModel.getDescription());
        oldCategory.setImageData(categoryModel.getImageData());

        categoryRepository.save(oldCategory);

        return true;
    }
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public boolean deleteCategory(Integer categoryId) {
        Category oldCategory = categoryRepository.findById(categoryId).orElse(null);
        if (oldCategory == null) return false;

        oldCategory.setDeleted(true);
        oldCategory.setDeleteDate(new Date());

        List<Recipe> recipes = recipeRepository.findByCategoryIdAndDeletedFalse(oldCategory.getId());

        for (Recipe recipe : recipes) {
            recipe.setDeleted(true);
            recipe.setDeleteDate(new Date());
        }

        categoryRepository.save(oldCategory);
        recipeRepository.saveAll(recipes);

        return true;

    }
}
