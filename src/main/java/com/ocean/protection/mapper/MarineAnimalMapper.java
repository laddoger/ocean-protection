package com.ocean.protection.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ocean.protection.entity.MarineAnimal;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface MarineAnimalMapper extends BaseMapper<MarineAnimal> {
    List<MarineAnimal> selectAllAnimals();
    List<MarineAnimal> searchAnimals(String keyword);
    MarineAnimal getAnimalDetail(Long id);
} 