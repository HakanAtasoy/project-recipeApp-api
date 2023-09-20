package com.atasoy.recipe.controller;

import com.atasoy.recipe.dao.FavoritePageResponse;
import com.atasoy.recipe.model.FavoriteModel;
import com.atasoy.recipe.service.impl.FavoriteServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin("http://localhost:4200") // Specify allowed origin(s)
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteServiceImpl favoriteService;

    public FavoriteController(FavoriteServiceImpl favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<FavoritePageResponse> getAllFavorites(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "12") Integer pageSize) {
        return favoriteService.getAllFavorites(page, pageSize);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Optional<FavoriteModel> getFavorite(@PathVariable("id") Integer favoriteId) {
        return favoriteService.getFavorite(favoriteId);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public void addFavorite(@RequestParam Integer recipeId) {
        favoriteService.addFavorite(recipeId);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public boolean deleteFavorite(@PathVariable("id") Integer recipeId) {
        return favoriteService.deleteFavorite(recipeId);
    }

    @DeleteMapping("/remove/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public boolean removeFromFavorites(@PathVariable("id") Integer favoriteId) {
        return favoriteService.removeFromFavorites(favoriteId);
    }

}
