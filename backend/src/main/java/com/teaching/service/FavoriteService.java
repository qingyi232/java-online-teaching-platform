package com.teaching.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.teaching.entity.ResourceFavorite;

public interface FavoriteService {

    void addFavorite(Long resourceId, Long userId);

    void removeFavorite(Long resourceId, Long userId);

    boolean isFavorited(Long resourceId, Long userId);

    IPage<ResourceFavorite> getFavoritePage(int page, int size, Long userId);
}
