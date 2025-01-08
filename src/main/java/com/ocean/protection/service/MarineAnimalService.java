package com.ocean.protection.service;

import com.ocean.protection.entity.MarineAnimal;
import com.ocean.protection.entity.MarineAnimalCategory;
import java.util.List;

public interface MarineAnimalService {
    List<MarineAnimalCategory> getAllCategories();
    List<MarineAnimal> getFeaturedAnimals();
    List<MarineAnimal> getAnimalsByCategory(Long categoryId);
    List<MarineAnimal> searchAnimals(String keyword);
    MarineAnimal getAnimalDetail(Long id);
    List<MarineAnimal> getAllAnimals();
} 