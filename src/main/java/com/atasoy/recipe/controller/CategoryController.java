package com.atasoy.recipe.controller;

import com.atasoy.recipe.dao.CategoryPageResponse;
import com.atasoy.recipe.model.CategoryModel;
import com.atasoy.recipe.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@CrossOrigin("http://localhost:4200") // Specify allowed origin(s)
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping()
    public ResponseEntity<CategoryPageResponse> getAllCategories(Integer page, Integer pageSize) {
        CategoryPageResponse categoryPage = categoryService.getAllCategories(page, pageSize);
        return ResponseEntity.ok(categoryPage);
    }

    @GetMapping("{id}")
    public Optional<CategoryModel> getCategory(@PathVariable("id") Integer categoryId) {
        return categoryService.getCategory(categoryId);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public void addCategory(@Valid @RequestPart CategoryModel categoryModel,
                            @RequestPart("imageFile") MultipartFile imageFile) throws IOException {
        categoryService.addCategory(categoryModel, imageFile);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public boolean updateCategory(@Valid @RequestPart CategoryModel categoryModel,
                                  @RequestPart("imageFile") MultipartFile imageFile) {
        return categoryService.updateCategory(categoryModel, imageFile);
    }

    @DeleteMapping("{id}")
    public boolean deleteCategory(@PathVariable("id") Integer categoryId) {
        return categoryService.deleteCategory(categoryId);
    }
}

