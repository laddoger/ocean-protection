package com.ocean.protection.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@TableName("volunteer_organization")
public class VolunteerOrganization {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String name;
    
    private String description;
    
    @TableField(exist = false)
    private String location;
    
    @TableField(exist = false)
    private String contactInfo;
    
    @TableField("member_count")
    private Integer memberCount;
    
    @TableField(exist = false)
    private String imageUrl;
    
    @TableField("founder_id")
    private Long founderId;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
    
    @TableLogic
    private Boolean deleted;
    
    @TableField(exist = false)
    private List<User> members;
    
    @TableField(exist = false)
    private List<VolunteerActivity> activities;
    
    @TableField(exist = false)
    private User founder;
} 