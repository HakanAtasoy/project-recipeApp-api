package com.atasoy.recipe.dao;

import com.atasoy.recipe.model.FavoriteModel;

import java.util.List;

public class FavoritePageResponse {
    private List<FavoriteModel> content;
    private int page;
    private int totalPages;
    private long totalItems;
    private int pageSize = 12;

    public List<FavoriteModel> getContent() {
        return content;
    }

    public void setContent(List<FavoriteModel> content) {
        this.content = content;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
