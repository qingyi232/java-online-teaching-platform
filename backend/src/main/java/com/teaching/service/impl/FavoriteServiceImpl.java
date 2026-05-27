package com.teaching.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.teaching.common.BusinessException;
import com.teaching.entity.ResourceFavorite;
import com.teaching.mapper.ResourceFavoriteMapper;
import com.teaching.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private ResourceFavoriteMapper favoriteMapper;

    @Override
    public void addFavorite(Long resourceId, Long userId) {
        Long count = favoriteMapper.selectCount(
                new LambdaQueryWrapper<ResourceFavorite>()
                        .eq(ResourceFavorite::getResourceId, resourceId)
                        .eq(ResourceFavorite::getUserId, userId));
        if (count > 0) {
            throw new BusinessException("已收藏该资源");
        }
        ResourceFavorite favorite = new ResourceFavorite();
        favorite.setResourceId(resourceId);
        favorite.setUserId(userId);
        favoriteMapper.insert(favorite);
    }

    @Override
    public void removeFavorite(Long resourceId, Long userId) {
        favoriteMapper.delete(
                new LambdaQueryWrapper<ResourceFavorite>()
                        .eq(ResourceFavorite::getResourceId, resourceId)
                        .eq(ResourceFavorite::getUserId, userId));
    }

    @Override
    public boolean isFavorited(Long resourceId, Long userId) {
        Long count = favoriteMapper.selectCount(
                new LambdaQueryWrapper<ResourceFavorite>()
                        .eq(ResourceFavorite::getResourceId, resourceId)
                        .eq(ResourceFavorite::getUserId, userId));
        return count > 0;
    }

    @Override
    public IPage<ResourceFavorite> getFavoritePage(int page, int size, Long userId) {
        return favoriteMapper.selectFavoritePage(new Page<>(page, size), userId);
    }
}
