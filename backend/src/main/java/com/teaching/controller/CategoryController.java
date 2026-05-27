package com.teaching.controller;

import com.teaching.common.Result;
import com.teaching.entity.ResourceCategory;
import com.teaching.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resource/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public Result<List<ResourceCategory>> getAllCategories() {
        return Result.success(categoryService.getAllCategories());
    }

    @PostMapping
    public Result<?> addCategory(@RequestBody ResourceCategory category) {
        categoryService.addCategory(category);
        return Result.success("添加成功");
    }

    @PutMapping
    public Result<?> updateCategory(@RequestBody ResourceCategory category) {
        categoryService.updateCategory(category);
        return Result.success("更新成功");
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return Result.success("删除成功");
    }
}
