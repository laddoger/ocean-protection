package com.ocean.protection.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@TableName("organization_member")
public class OrganizationMember {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long organizationId;
    private Long userId;
    private String role;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime joinTime;
    
    @TableLogic
    private Boolean deleted;
    
    @TableField(exist = false)
    private User user;
} 