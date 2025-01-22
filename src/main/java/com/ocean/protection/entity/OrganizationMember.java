package com.ocean.protection.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("organization_member")
public class OrganizationMember {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long organizationId;
    
    private Long userId;
    
    private String role;
    
    @TableLogic
    @TableField(value = "deleted", fill = FieldFill.INSERT)
    private Boolean deleted = false;
} 