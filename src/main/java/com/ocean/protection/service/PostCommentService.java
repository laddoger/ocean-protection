package com.ocean.protection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ocean.protection.entity.PostComment;
import java.util.List;

public interface PostCommentService extends IService<PostComment> {
    List<PostComment> getCommentsByPostId(Long postId);
} 