package com.teaching.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.teaching.entity.TeachingResource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TeachingResourceMapper extends BaseMapper<TeachingResource> {

    IPage<TeachingResource> selectResourcePage(Page<TeachingResource> page,
                                                @Param("title") String title,
                                                @Param("categoryId") Long categoryId,
                                                @Param("fileType") String fileType,
                                                @Param("status") Integer status,
                                                @Param("uploadUserId") Long uploadUserId);

    TeachingResource selectResourceDetail(@Param("id") Long id);
}
