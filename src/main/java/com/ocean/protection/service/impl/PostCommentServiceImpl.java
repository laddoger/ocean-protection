package com.ocean.protection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ocean.protection.entity.PostComment;
import com.ocean.protection.mapper.PostCommentMapper;
import com.ocean.protection.service.PostCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostCommentServiceImpl extends ServiceImpl<PostCommentMapper, PostComment> implements PostCommentService {
    
    @Override
    public List<PostComment> getCommentsByPostId(Long postId) {
        LambdaQueryWrapper<PostComment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PostComment::getPostId, postId)
              .eq(PostComment::getDeleted, false)
              .orderByDesc(PostComment::getCreatedTime);
        return list(wrapper);
    }
} 