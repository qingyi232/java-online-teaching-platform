package com.teaching.service;

import com.teaching.entity.ResourceCategory;

import java.util.List;

public interface CategoryService {

    List<ResourceCategory> getAllCategories();

    void addCategory(ResourceCategory category);

    void updateCategory(ResourceCategory category);

    void deleteCategory(Long id);
}
