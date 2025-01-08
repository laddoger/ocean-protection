package com.ocean.protection.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@TableName("forum_like")
public class ForumLike {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long postId;
    private Long userId;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
} 