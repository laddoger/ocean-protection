package com.ocean.protection.controller;

import com.ocean.protection.common.result.Result;
import com.ocean.protection.entity.MarineAnimal;
import com.ocean.protection.entity.MarineAnimalCategory;
import com.ocean.protection.service.MarineAnimalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/animals")
@RequiredArgsConstructor
public class MarineAnimalController {
    
    private final MarineAnimalService marineAnimalService;

    @GetMapping("/categories")
    public Result<List<MarineAnimalCategory>> getCategories() {
        return Result.success(marineAnimalService.getAllCategories());
    }

    @GetMapping("/featured")
    public Result<List<MarineAnimal>> getFeatured() {
        return Result.success(marineAnimalService.getFeaturedAnimals());
    }

    @GetMapping("/category/{categoryId}")
    public Result<List<MarineAnimal>> getByCategory(@PathVariable Long categoryId) {
        return Result.success(marineAnimalService.getAnimalsByCategory(categoryId));
    }

    @GetMapping("/search")
    public Result<List<MarineAnimal>> search(@RequestParam String keyword) {
        return Result.success(marineAnimalService.searchAnimals(keyword));
    }

    @GetMapping("/{id}")
    public Result<MarineAnimal> getDetail(@PathVariable Long id) {
        return Result.success(marineAnimalService.getAnimalDetail(id));
    }

    @GetMapping
    public Result<List<MarineAnimal>> getAll() {
        return Result.success(marineAnimalService.getAllAnimals());
    }
} 