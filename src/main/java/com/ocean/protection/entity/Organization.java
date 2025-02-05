package com.ocean.protection.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("volunteer_organization")
public class Organization {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("name")
    private String name;
    
    @TableField("description")
    private String description;
    
    @TableField("founder_id")
    private Long founderId;
    
    @TableField("member_count")
    private Integer memberCount;
    
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
    
    @TableLogic
    @TableField("deleted")
    private Boolean deleted;
} 