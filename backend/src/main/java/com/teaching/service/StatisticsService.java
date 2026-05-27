package com.teaching.service;

import java.util.Map;

public interface StatisticsService {

    Map<String, Object> getDashboardStats();

    Map<String, Object> getViewStats(int days);

    Map<String, Object> getDownloadStats(int days);

    Map<String, Object> getCategoryStats();

    Map<String, Object> getTopResources(int limit);
}
