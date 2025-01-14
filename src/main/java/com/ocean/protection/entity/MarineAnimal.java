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
    private String name;
    private String scientificName;
    private String coverImage;
    private String description;
    private String habitat;
    private String conservationStatus;
    private Boolean featured;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
    
    @TableLogic
    private Boolean deleted;
} 