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
    private Long activityId;
    private Long userId;
    private LocalDateTime joinTime;
    
    @TableLogic
    private Boolean deleted;
    
    @TableField(exist = false)
    private User user;
} 