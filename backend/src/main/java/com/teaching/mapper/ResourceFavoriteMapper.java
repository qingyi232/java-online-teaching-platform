package com.teaching.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.teaching.entity.ResourceFavorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ResourceFavoriteMapper extends BaseMapper<ResourceFavorite> {

    IPage<ResourceFavorite> selectFavoritePage(Page<ResourceFavorite> page, @Param("userId") Long userId);
}
