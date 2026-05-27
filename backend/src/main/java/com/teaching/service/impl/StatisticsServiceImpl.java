package com.teaching.service.impl;

import com.teaching.mapper.*;
import com.teaching.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private TeachingResourceMapper resourceMapper;

    @Autowired
    private ResourceViewLogMapper viewLogMapper;

    @Autowired
    private ResourceDownloadLogMapper downloadLogMapper;

    @Override
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userMapper.selectCount(null));
        stats.put("totalResources", resourceMapper.selectCount(null));
        stats.put("totalViews", viewLogMapper.countTotalViews());
        stats.put("totalDownloads", downloadLogMapper.countTotalDownloads());
        return stats;
    }

    @Override
    public Map<String, Object> getViewStats(int days) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("viewsByDate", viewLogMapper.countViewsByDate(days));
        return stats;
    }

    @Override
    public Map<String, Object> getDownloadStats(int days) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("downloadsByDate", downloadLogMapper.countDownloadsByDate(days));
        return stats;
    }

    @Override
    public Map<String, Object> getCategoryStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("viewsByCategory", viewLogMapper.countViewsByCategory());
        return stats;
    }

    @Override
    public Map<String, Object> getTopResources(int limit) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("topResources", downloadLogMapper.topDownloadResources(limit));
        return stats;
    }
}
