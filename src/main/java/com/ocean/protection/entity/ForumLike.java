package com.ocean.protection.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("forum_like")
public class ForumLike {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("post_id")
    private Long postId;
    
    @TableField("user_id")
    private Long userId;
    
    @TableField(exist = false)
    private LocalDateTime createdTime;
} 