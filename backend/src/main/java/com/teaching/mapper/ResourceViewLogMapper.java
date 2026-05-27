package com.teaching.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.teaching.entity.ResourceViewLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ResourceViewLogMapper extends BaseMapper<ResourceViewLog> {

    List<Map<String, Object>> countViewsByDate(@Param("days") int days);

    List<Map<String, Object>> countViewsByCategory();

    Long countTotalViews();
}
