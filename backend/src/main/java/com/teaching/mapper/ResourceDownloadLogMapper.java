package com.teaching.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.teaching.entity.ResourceDownloadLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ResourceDownloadLogMapper extends BaseMapper<ResourceDownloadLog> {

    List<Map<String, Object>> countDownloadsByDate(@Param("days") int days);

    List<Map<String, Object>> topDownloadResources(@Param("limit") int limit);

    Long countTotalDownloads();
}
