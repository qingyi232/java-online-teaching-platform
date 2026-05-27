package com.teaching.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.teaching.entity.ResourceComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ResourceCommentMapper extends BaseMapper<ResourceComment> {

    List<ResourceComment> selectCommentsByResourceId(@Param("resourceId") Long resourceId);
}
