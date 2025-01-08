package com.ocean.protection.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ocean.protection.entity.Post;
import com.ocean.protection.mapper.PostMapper;
import com.ocean.protection.service.PostService;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {
    // 可以实现自定义方法
} 