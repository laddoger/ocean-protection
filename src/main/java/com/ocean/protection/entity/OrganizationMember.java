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
    
    @TableField("organization_id")
    private Long organizationId;
    
    @TableField("user_id")
    private Long userId;
    
    private String role;
    
    @TableField("join_time")
    private LocalDateTime joinTime;
    
    @TableField(exist = false)
    private LocalDateTime createdTime;
    
    @TableField(exist = false)
    private LocalDateTime updatedTime;
    
    @TableLogic
    private Boolean deleted;
    
    @TableField(exist = false)
    private User user;
    
    @TableField(exist = false)
    private VolunteerOrganization organization;
} 