package com.ocean.protection.service;

import com.ocean.protection.entity.MarineAnimal;
import java.util.List;

public interface MarineAnimalService {
    List<MarineAnimal> getAllAnimals();
    List<MarineAnimal> getFeaturedAnimals();
    List<MarineAnimal> searchAnimals(String keyword);
    MarineAnimal getAnimalDetail(Long id);
} 