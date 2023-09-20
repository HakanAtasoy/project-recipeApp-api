package com.atasoy.recipe.service.impl;

import com.atasoy.recipe.dao.FavoritePageResponse;
import com.atasoy.recipe.entity.Favorite;
import com.atasoy.recipe.mapper.FavoriteMapper;
import com.atasoy.recipe.model.FavoriteModel;
import com.atasoy.recipe.repo.FavoriteRepository;
import com.atasoy.recipe.service.FavoriteService;
import com.atasoy.recipe.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserService userService;

    public FavoriteServiceImpl(FavoriteRepository favoriteRepository, UserService userService) {
        this.favoriteRepository = favoriteRepository;
        this.userService = userService;
    }

    @Transactional
    public ResponseEntity<FavoritePageResponse> getAllFavorites(Integer page, Integer pageSize) {
        Integer userId = userService.getUserFromSession().getId();
        Pageable pageable = PageRequest.of(page - 1, pageSize); // Page numbers start from 0
        Page<Favorite> favoritePage = favoriteRepository.findByUserIdAndDeletedFalseOrderByCreateDateAsc(userId, pageable);
        FavoritePageResponse response = new FavoritePageResponse();
        response.setContent(favoritePage.getContent().stream()
                .map(FavoriteMapper::toFavoriteModel)
                .collect(Collectors.toList()));
        response.setPage(favoritePage.getNumber() + 1);
        response.setTotalPages(favoritePage.getTotalPages());
        response.setTotalItems(favoritePage.getTotalElements());
        response.setPageSize(pageSize);
        return ResponseEntity.ok(response);
    }

    public Optional<FavoriteModel> getFavorite(Integer favoriteId) {
        return favoriteRepository.findById(favoriteId).filter(r -> !r.isDeleted()).map(FavoriteMapper::toFavoriteModel);

    }

    public void addFavorite(Integer recipeId) {
        Favorite favorite = new Favorite();
        favorite.setRecipeId(recipeId);
        Integer userId = userService.getUserFromSession().getId();
        favorite.setUserId(userId);
        favorite.setCreateDate(new Date());
        favoriteRepository.save(favorite);
    }

    public boolean deleteFavorite(Integer recipeId) {

        Integer userId = userService.getUserFromSession().getId();
        Favorite oldFavorite = favoriteRepository.findByUserIdAndRecipeIdAndDeletedFalse(userId, recipeId).orElse(null);


        if (oldFavorite == null || !Objects.equals(oldFavorite.getUser().getId(), userId))
            return false;

        oldFavorite.setDeleted(true);
        oldFavorite.setDeleteDate(new Date());

        favoriteRepository.save(oldFavorite);
        return true;
    }

    public boolean removeFromFavorites(Integer favoriteId) {

        Integer userId = userService.getUserFromSession().getId();
        Favorite oldFavorite = favoriteRepository.findByIdAndDeletedFalse(favoriteId).orElse(null);
        if (oldFavorite == null || !Objects.equals(oldFavorite.getUser().getId(), userId))
            return false;

        oldFavorite.setDeleted(true);
        oldFavorite.setDeleteDate(new Date());

        favoriteRepository.save(oldFavorite);
        return true;
    }

    public boolean checkIfLiked(Integer userId, Integer recipeId) {
        return favoriteRepository.findByUserIdAndRecipeIdAndDeletedFalse(userId, recipeId).isPresent();
    }

    public Integer numberOfLikes(Integer recipeId) {
        return favoriteRepository.countByRecipeIdAndDeletedFalse(recipeId);
    }

}
