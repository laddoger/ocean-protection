package com.ocean.protection.config;

import com.ocean.protection.entity.OceanPollutant;
import com.ocean.protection.mapper.OceanPollutantMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final OceanPollutantMapper pollutantMapper;

    @Override
    public void run(String... args) {
        // 检查是否已有数据
        if (pollutantMapper.selectCount(null) > 0) {
            log.info("数据库中已存在污染物数据，跳过初始化");
            return;
        }

        log.info("开始初始化污染物数据...");
        
        // 初始化污染物数据
        List<OceanPollutant> pollutants = Arrays.asList(
            createPollutant("塑料袋", "塑料制品", 35.50, 
                "可能被海洋生物误食导致死亡；分解过程产生微塑料污染；破坏海洋生态系统",
                "减少使用一次性塑料制品；参与海滩清理活动；使用环保购物袋"),
            createPollutant("农药残留", "化学污染物", 15.20,
                "破坏海洋生态系统平衡；危害海洋生物健康；通过食物链危害人类健康",
                "控制农药使用量；发展有机农业；建立农药使用监管体系"),
            createPollutant("工业废水", "工业污染", 28.30,
                "含有重金属等有害物质；破坏水质；导致海洋生物死亡；影响渔业发展",
                "加强工业废水处理；提高环保标准；推广清洁生产技术"),
            createPollutant("生活垃圾", "生活废弃物", 12.50,
                "污染海水；破坏海滩景观；危害海洋生物生存",
                "加强垃圾分类；减少一次性用品使用；建立海岸清理机制"),
            createPollutant("石油泄漏", "石油污染", 8.50,
                "破坏海洋生态系统；导致海洋生物大规模死亡；影响沿海旅游业",
                "加强油轮安全管理；完善应急处理机制；发展清洁能源")
        );

        // 批量插入数据
        for (OceanPollutant pollutant : pollutants) {
            try {
                pollutantMapper.insert(pollutant);
                log.info("成功添加污染物: {}", pollutant.getName());
            } catch (Exception e) {
                log.error("添加污染物失败: {}", pollutant.getName(), e);
            }
        }

        log.info("污染物数据初始化完成");
    }

    private OceanPollutant createPollutant(String name, String category, 
            double percentage, String harmDescription, String protectionMeasures) {
        OceanPollutant pollutant = new OceanPollutant();
        pollutant.setName(name);
        pollutant.setCategory(category);
        pollutant.setPercentage(BigDecimal.valueOf(percentage));
        pollutant.setHarmDescription(harmDescription);
        pollutant.setProtectionMeasures(protectionMeasures);
        pollutant.setDeleted(false);
        return pollutant;
    }
} 