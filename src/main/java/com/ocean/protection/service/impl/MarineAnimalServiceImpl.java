package com.ocean.protection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ocean.protection.entity.MarineAnimal;
import com.ocean.protection.entity.MarineAnimalCategory;
import com.ocean.protection.mapper.MarineAnimalCategoryMapper;
import com.ocean.protection.mapper.MarineAnimalMapper;
import com.ocean.protection.service.MarineAnimalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarineAnimalServiceImpl implements MarineAnimalService {

    private final MarineAnimalMapper marineAnimalMapper;
    private final MarineAnimalCategoryMapper categoryMapper;

    @Override
    public List<MarineAnimalCategory> getAllCategories() {
        return categoryMapper.selectList(null);
    }

    @Override
    public List<MarineAnimal> getFeaturedAnimals() {
        LambdaQueryWrapper<MarineAnimal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MarineAnimal::getFeatured, true);
        return marineAnimalMapper.selectList(wrapper);
    }

    @Override
    public List<MarineAnimal> getAnimalsByCategory(Long categoryId) {
        LambdaQueryWrapper<MarineAnimal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MarineAnimal::getCategoryId, categoryId);
        return marineAnimalMapper.selectList(wrapper);
    }

    @Override
    public List<MarineAnimal> searchAnimals(String keyword) {
        LambdaQueryWrapper<MarineAnimal> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(MarineAnimal::getName, keyword)
              .or()
              .like(MarineAnimal::getScientificName, keyword)
              .or()
              .like(MarineAnimal::getDescription, keyword);
        return marineAnimalMapper.selectList(wrapper);
    }

    @Override
    public MarineAnimal getAnimalDetail(Long id) {
        return marineAnimalMapper.selectById(id);
    }

    @Override
    public List<MarineAnimal> getAllAnimals() {
        return marineAnimalMapper.selectList(null);
    }
} 