package com.teaching.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.teaching.common.BusinessException;
import com.teaching.entity.ResourceCategory;
import com.teaching.entity.TeachingResource;
import com.teaching.mapper.ResourceCategoryMapper;
import com.teaching.mapper.TeachingResourceMapper;
import com.teaching.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private ResourceCategoryMapper categoryMapper;

    @Autowired
    private TeachingResourceMapper resourceMapper;

    @Override
    public List<ResourceCategory> getAllCategories() {
        return categoryMapper.selectList(
                new LambdaQueryWrapper<ResourceCategory>().orderByAsc(ResourceCategory::getSort));
    }

    @Override
    public void addCategory(ResourceCategory category) {
        Long count = categoryMapper.selectCount(
                new LambdaQueryWrapper<ResourceCategory>().eq(ResourceCategory::getName, category.getName()));
        if (count > 0) {
            throw new BusinessException("分类名称已存在");
        }
        categoryMapper.insert(category);
    }

    @Override
    public void updateCategory(ResourceCategory category) {
        categoryMapper.updateById(category);
    }

    @Override
    public void deleteCategory(Long id) {
        Long resourceCount = resourceMapper.selectCount(
                new LambdaQueryWrapper<TeachingResource>().eq(TeachingResource::getCategoryId, id));
        if (resourceCount > 0) {
            throw new BusinessException("该分类下存在资源，无法删除");
        }
        categoryMapper.deleteById(id);
    }
}
