package com.teaching.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.teaching.common.Result;
import com.teaching.entity.ResourceFavorite;
import com.teaching.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/{resourceId}")
    public Result<?> addFavorite(@PathVariable Long resourceId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        favoriteService.addFavorite(resourceId, userId);
        return Result.success("收藏成功");
    }

    @DeleteMapping("/{resourceId}")
    public Result<?> removeFavorite(@PathVariable Long resourceId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        favoriteService.removeFavorite(resourceId, userId);
        return Result.success("取消收藏");
    }

    @GetMapping("/check/{resourceId}")
    public Result<Map<String, Boolean>> checkFavorite(@PathVariable Long resourceId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        Map<String, Boolean> data = new HashMap<>();
        data.put("favorited", favoriteService.isFavorited(resourceId, userId));
        return Result.success(data);
    }

    @GetMapping("/list")
    public Result<IPage<ResourceFavorite>> getFavorites(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(favoriteService.getFavoritePage(page, size, userId));
    }
}
