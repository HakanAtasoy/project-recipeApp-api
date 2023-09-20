package com.atasoy.recipe.service;

import com.atasoy.recipe.dao.CategoryPageResponse;
import com.atasoy.recipe.model.CategoryModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface CategoryService {
    public CategoryPageResponse getAllCategories(Integer page, Integer pageSize);

    public Optional<CategoryModel> getCategory(Integer categoryId);

    public void addCategory(CategoryModel categoryModel, MultipartFile imageFile) throws IOException;

    public boolean updateCategory(CategoryModel categoryModel, MultipartFile imageFile);

    public void deleteCategory(Integer categoryId);
}
