package com.ocean.protection.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@TableName("activity_participant")
public class ActivityParticipant {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("activity_id")
    private Long activityId;
    
    @TableField("user_id")
    private Long userId;
    
    @TableField("join_time")
    private LocalDateTime joinTime;
    
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
    
    @TableLogic
    private Boolean deleted;
    
    @TableField(exist = false)
    private User user;
    
    @TableField(exist = false)
    private VolunteerActivity activity;
} 