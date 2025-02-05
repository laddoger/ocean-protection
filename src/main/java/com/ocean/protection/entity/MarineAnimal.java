package com.ocean.protection.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@TableName("marine_animal")
public class MarineAnimal {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String name;            // 动物名称
    
    @TableField("scientific_name")
    private String scientificName;  // 动物学名
    
    private String category;        // 动物类别
    
    private String description;     // 动物简介
    
    @TableField("conservation_status")
    private String conservationStatus;  // 保护状态
    
    private String habitat;         // 栖息地
    
    @TableField("image_url")
    private String imageUrl;        // 动物图片URL
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
    
    @TableLogic
    private Boolean deleted;
    
    @TableField(exist = false)
    private MarineAnimalCategory categoryInfo;
} 