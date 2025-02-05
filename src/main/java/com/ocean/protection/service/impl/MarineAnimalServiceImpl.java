package com.ocean.protection.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ocean.protection.entity.MarineAnimal;
import com.ocean.protection.mapper.MarineAnimalMapper;
import com.ocean.protection.service.MarineAnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarineAnimalServiceImpl extends ServiceImpl<MarineAnimalMapper, MarineAnimal> implements MarineAnimalService {
    
    @Autowired
    private MarineAnimalMapper marineAnimalMapper;

    @Override
    public List<MarineAnimal> getAllAnimals() {
        return marineAnimalMapper.selectAllAnimals();
    }

    @Override
    public List<MarineAnimal> searchAnimals(String keyword) {
        return marineAnimalMapper.searchAnimals(keyword);
    }

    @Override
    public MarineAnimal getAnimalDetail(Long id) {
        return marineAnimalMapper.getAnimalDetail(id);
    }
} 