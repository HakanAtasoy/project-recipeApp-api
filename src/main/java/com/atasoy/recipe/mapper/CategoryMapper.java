package com.atasoy.recipe.mapper;

import com.atasoy.recipe.entity.Category;
import com.atasoy.recipe.model.CategoryModel;

import java.util.Date;

public class CategoryMapper {
    public static CategoryModel toCategoryModel(Category category) {
        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setName(category.getName());
        categoryModel.setDescription(category.getDescription());
        categoryModel.setImageData(category.getImageData());
        categoryModel.setId(category.getId());
        return categoryModel;
    }

    public static Category toCategory(CategoryModel categoryModel) {
        Category category = new Category();
        category.setDescription(categoryModel.getDescription());
        category.setName(categoryModel.getName());
        category.setImageData(categoryModel.getImageData());
        category.setDeleted(false);
        category.setCreateDate(new Date());
        category.setUpdateDate(new Date());
        category.setDeleteDate(null);
        return category;
    }
}
