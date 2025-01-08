package com.ocean.protection.controller;

import com.ocean.protection.common.result.Result;
import com.ocean.protection.entity.OceanPollutant;
import com.ocean.protection.service.OceanPollutantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pollution")
@RequiredArgsConstructor
@Slf4j
public class OceanPollutantController {

    private final OceanPollutantService pollutantService;

    @GetMapping("/percentages")
    public Result<List<OceanPollutant>> getPollutantPercentages() {
        try {
            List<OceanPollutant> pollutants = pollutantService.getPollutantPercentages();
            log.info("获取污染物占比成功，数据条数：{}", pollutants.size());
            return Result.success(pollutants);
        } catch (Exception e) {
            log.error("获取污染物占比失败: {}", e.getMessage());
            return Result.error("获取污染物占比失败");
        }
    }

    @GetMapping("/list")
    public Result<List<OceanPollutant>> getAllPollutants() {
        try {
            List<OceanPollutant> pollutants = pollutantService.getAllPollutants();
            return Result.success(pollutants);
        } catch (Exception e) {
            log.error("获取污染物列表失败: {}", e.getMessage());
            return Result.error("获取污染物列表失败");
        }
    }

    @GetMapping("/search")
    public Result<OceanPollutant> searchPollutant(@RequestParam String name) {
        try {
            OceanPollutant pollutant = pollutantService.searchPollutant(name);
            if (pollutant == null) {
                return Result.error("该物品不是海洋污染物");
            }
            return Result.success(pollutant);
        } catch (Exception e) {
            log.error("搜索污染物失败: {}", e.getMessage());
            return Result.error("搜索污染物失败");
        }
    }
} 