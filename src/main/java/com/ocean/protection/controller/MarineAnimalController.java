package com.ocean.protection.controller;

import com.ocean.protection.common.result.Result;
import com.ocean.protection.entity.MarineAnimal;
import com.ocean.protection.service.MarineAnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/animals")
public class MarineAnimalController {
    
    @Autowired
    private MarineAnimalService marineAnimalService;

    @GetMapping
    public Result<List<MarineAnimal>> getAll() {
        return Result.success(marineAnimalService.getAllAnimals());
    }

    @GetMapping("/search")
    public Result<List<MarineAnimal>> search(@RequestParam String keyword) {
        return Result.success(marineAnimalService.searchAnimals(keyword));
    }

    @GetMapping("/{id}")
    public Result<MarineAnimal> getDetail(@PathVariable Long id) {
        return Result.success(marineAnimalService.getAnimalDetail(id));
    }
} 