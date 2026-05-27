package com.teaching.controller;

import com.teaching.common.Result;
import com.teaching.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/dashboard")
    public Result<Map<String, Object>> getDashboardStats() {
        return Result.success(statisticsService.getDashboardStats());
    }

    @GetMapping("/views")
    public Result<Map<String, Object>> getViewStats(@RequestParam(defaultValue = "7") int days) {
        return Result.success(statisticsService.getViewStats(days));
    }

    @GetMapping("/downloads")
    public Result<Map<String, Object>> getDownloadStats(@RequestParam(defaultValue = "7") int days) {
        return Result.success(statisticsService.getDownloadStats(days));
    }

    @GetMapping("/category")
    public Result<Map<String, Object>> getCategoryStats() {
        return Result.success(statisticsService.getCategoryStats());
    }

    @GetMapping("/top")
    public Result<Map<String, Object>> getTopResources(@RequestParam(defaultValue = "10") int limit) {
        return Result.success(statisticsService.getTopResources(limit));
    }
}
