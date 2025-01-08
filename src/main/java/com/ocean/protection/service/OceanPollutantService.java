package com.ocean.protection.service;

import com.ocean.protection.entity.OceanPollutant;
import java.util.List;
import java.util.Map;

public interface OceanPollutantService {
    List<OceanPollutant> getPollutantPercentages();
    List<OceanPollutant> getAllPollutants();
    OceanPollutant searchPollutant(String name);
} 