package com.ocean.protection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ocean.protection.entity.OceanPollutant;
import com.ocean.protection.mapper.OceanPollutantMapper;
import com.ocean.protection.service.OceanPollutantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class OceanPollutantServiceImpl implements OceanPollutantService {

    private final OceanPollutantMapper pollutantMapper;

    @Override
    public List<OceanPollutant> getPollutantPercentages() {
        try {
            LambdaQueryWrapper<OceanPollutant> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(OceanPollutant::getDeleted, false)
                  .orderByDesc(OceanPollutant::getPercentage);
            
            List<OceanPollutant> pollutants = pollutantMapper.selectList(wrapper);
            if (pollutants.isEmpty()) {
                log.warn("No pollutant data found");
                return new ArrayList<>();
            }
            
            return pollutants;
        } catch (Exception e) {
            log.error("Error getting pollutant percentages", e);
            throw new RuntimeException("获取污染物占比失败", e);
        }
    }

    @Override
    public List<OceanPollutant> getAllPollutants() {
        return pollutantMapper.selectList(null);
    }

    @Override
    public OceanPollutant searchPollutant(String name) {
        LambdaQueryWrapper<OceanPollutant> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(OceanPollutant::getName, name);
        return pollutantMapper.selectOne(wrapper);
    }
} 