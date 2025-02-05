package com.ocean.protection.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@TableName("post")
public class ForumPost {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String title;
    
    private String content;
    
    @TableField("user_id")
    private Long userId;
    
    private String tag;
    
    @TableField("images_json")
    private String imagesJson;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
    
    @TableLogic
    private Boolean deleted;
    
    @TableField(exist = false)
    private User user;
    
    @TableField(exist = false)
    private List<ForumComment> comments;
    
    @TableField("view_count")
    private Integer viewCount;
    
    @TableField("like_count")
    private Integer likeCount;
    
    @TableField("comment_count")
    private Integer commentCount;
    
    @TableField(exist = false)
    private Boolean isLiked;
} 