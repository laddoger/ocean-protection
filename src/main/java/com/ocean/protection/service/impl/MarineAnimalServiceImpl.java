package com.ocean.protection.service.impl;

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