package com.ocean.protection.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@TableName(value = "marine_animal", excludeProperty = {"coverImage", "featured"})
public class MarineAnimal {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("name")
    private String name;            // 动物名称
    
    @TableField("scientific_name")
    private String scientificName;  // 动物学名
    
    @TableField("category")
    private String category;        // 动物类别
    
    @TableField("description")
    private String description;     // 动物简介
    
    @TableField("conservation_status")
    private String conservationStatus;  // 保护状态
    
    @TableField("habitat")
    private String habitat;         // 栖息地
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
    
    @TableLogic
    @TableField("deleted")
    private Boolean deleted;
} 