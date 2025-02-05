package com.ocean.protection.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Getter
@Setter
@TableName("ocean_pollutant")
public class OceanPollutant {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String name;
    
    private String category;
    
    private Double percentage;
    
    @TableField("harm_description")
    private String harmDescription;
    
    @TableField("protection_measures")
    private String protectionMeasures;
    
    @TableField("image_url")
    private String imageUrl;        // 污染物图片URL
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
    
    @TableLogic
    private Boolean deleted;
} 