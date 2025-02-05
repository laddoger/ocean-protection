package com.ocean.protection.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ocean.protection.entity.MarineAnimal;
import com.ocean.protection.entity.OceanPollutant;
import com.ocean.protection.mapper.MarineAnimalMapper;
import com.ocean.protection.mapper.OceanPollutantMapper;
import com.ocean.protection.service.ForumService;
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
    private final MarineAnimalMapper marineAnimalMapper;
    private final ForumService forumService;

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
                "加强油轮安全管理；完善应急处理机制；发展清洁能源"),
            createPollutant("放射性物质", "特殊污染物", 2.10, 
                "长期危害；影响生物基因；污染范围广", 
                "严控核废料处理；加强监测；建立应急机制")
        );

        // 批量插入数据
        for (OceanPollutant pollutant : pollutants) {
            try {
                if (pollutantMapper.selectCount(
                    new LambdaQueryWrapper<OceanPollutant>()
                        .eq(OceanPollutant::getName, pollutant.getName())
                ) == 0) {
                    pollutantMapper.insert(pollutant);
                    log.info("添加污染物: {}", pollutant.getName());
                }
            } catch (Exception e) {
                log.error("添加污染物失败: {}", pollutant.getName(), e);
            }
        }

        log.info("污染物数据初始化完成");

        // 更新所有帖子的评论数
        try {
            forumService.updateAllPostCommentCounts();
            log.info("帖子评论数更新完成");
        } catch (Exception e) {
            log.error("更新帖子评论数失败", e);
        }

        // 初始化海洋动物数据
        List<MarineAnimal> animals = Arrays.asList(
            createAnimal("蓝鲸", "鲸类", "Balaenoptera musculus", 
                "世界上最大的动物，可以长到33米长，重达200吨。它们主要以磷虾为食，尽管体型庞大，但性情温和。",
                "濒危", "深海水域，主要分布在南极洲和北极周边海域"),
            createAnimal("海豚", "鲸类", "Delphinidae", 
                "聪明的海洋哺乳动物，具有高度的智力和社交能力，常常成群活动。",
                "易危", "世界各大洋的表层水域，既有近海也有远洋种类"),
            createAnimal("绿海龟", "海龟", "Chelonia mydas", 
                "绿海龟是最大的硬壳海龟之一，以其橄榄绿色的脂肪而得名。它们主要以海草和海藻为食。",
                "濒危", "热带和亚热带海域，在浅海区域觅食，在海滩产卵"),
            createAnimal("珊瑚", "珊瑚类", "Anthozoa", 
                "珊瑚是海洋生态系统的重要组成部分，为众多海洋生物提供栖息地和庇护所。",
                "易危", "热带浅海区域，需要充足的阳光和清澈的水质"),
            createAnimal("座头鲸", "鲸类", "Megaptera novaeangliae", 
                "座头鲸以其独特的歌声和跃出水面的行为而闻名，每年会进行长距离迁徙。",
                "易危", "全球海域，从极地到热带水域都有分布")
        );

        for (MarineAnimal animal : animals) {
            try {
                if (marineAnimalMapper.selectCount(
                    new LambdaQueryWrapper<MarineAnimal>()
                        .eq(MarineAnimal::getName, animal.getName())
                ) == 0) {
                    marineAnimalMapper.insert(animal);
                    log.info("添加海洋动物: {}", animal.getName());
                }
            } catch (Exception e) {
                log.error("添加海洋动物失败: {}", animal.getName(), e);
            }
        }
    }

    private OceanPollutant createPollutant(String name, String category, 
            double percentage, String harmDescription, String protectionMeasures) {
        OceanPollutant pollutant = new OceanPollutant();
        pollutant.setName(name);
        pollutant.setCategory(category);
        pollutant.setPercentage(Double.valueOf(percentage));
        pollutant.setHarmDescription(harmDescription);
        pollutant.setProtectionMeasures(protectionMeasures);
        pollutant.setDeleted(false);
        return pollutant;
    }

    private MarineAnimal createAnimal(String name, String category, String scientificName, 
            String description, String conservationStatus, String habitat) {
        MarineAnimal animal = new MarineAnimal();
        animal.setName(name);
        animal.setCategory(category);
        animal.setScientificName(scientificName);
        animal.setDescription(description);
        animal.setConservationStatus(conservationStatus);
        animal.setHabitat(habitat);
        animal.setDeleted(false);
        return animal;
    }
} 