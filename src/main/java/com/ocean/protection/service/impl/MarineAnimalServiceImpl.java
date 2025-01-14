package com.ocean.protection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ocean.protection.entity.MarineAnimal;
import com.ocean.protection.mapper.MarineAnimalMapper;
import com.ocean.protection.service.MarineAnimalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarineAnimalServiceImpl implements MarineAnimalService {
    
    private final MarineAnimalMapper marineAnimalMapper;

    @Override
    public List<MarineAnimal> getAllAnimals() {
        return marineAnimalMapper.selectList(
            new LambdaQueryWrapper<MarineAnimal>()
                .eq(MarineAnimal::getDeleted, false)
                .orderByDesc(MarineAnimal::getCreatedTime)
        );
    }

    @Override
    public List<MarineAnimal> getFeaturedAnimals() {
        return marineAnimalMapper.selectList(
            new LambdaQueryWrapper<MarineAnimal>()
                .eq(MarineAnimal::getFeatured, true)
                .eq(MarineAnimal::getDeleted, false)
                .orderByDesc(MarineAnimal::getCreatedTime)
        );
    }

    @Override
    public List<MarineAnimal> searchAnimals(String keyword) {
        return marineAnimalMapper.selectList(
            new LambdaQueryWrapper<MarineAnimal>()
                .eq(MarineAnimal::getDeleted, false)
                .and(wrapper -> wrapper
                    .like(MarineAnimal::getName, keyword)
                    .or()
                    .like(MarineAnimal::getScientificName, keyword)
                    .or()
                    .like(MarineAnimal::getDescription, keyword)
                )
                .orderByDesc(MarineAnimal::getCreatedTime)
        );
    }

    @Override
    public MarineAnimal getAnimalDetail(Long id) {
        return marineAnimalMapper.selectOne(
            new LambdaQueryWrapper<MarineAnimal>()
                .eq(MarineAnimal::getId, id)
                .eq(MarineAnimal::getDeleted, false)
        );
    }
} 