package com.ocean.protection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocean.protection.dto.CreatePostDTO;
import com.ocean.protection.entity.ForumComment;
import com.ocean.protection.entity.ForumLike;
import com.ocean.protection.entity.ForumPost;
import com.ocean.protection.entity.User;
import com.ocean.protection.mapper.ForumCommentMapper;
import com.ocean.protection.mapper.ForumLikeMapper;
import com.ocean.protection.mapper.ForumPostMapper;
import com.ocean.protection.mapper.UserMapper;
import com.ocean.protection.service.ForumService;
import com.ocean.protection.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class ForumServiceImpl extends ServiceImpl<ForumPostMapper, ForumPost> implements ForumService {

    private final ForumPostMapper postMapper;
    private final ForumCommentMapper commentMapper;
    private final ForumLikeMapper likeMapper;
    private final UserMapper userMapper;
    private final ObjectMapper objectMapper;
    
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public List<ForumPost> getPosts(Long currentUserId) {
        try {
            // 使用自定义的查询方法而不是 MyBatis-Plus 的默认方法
            List<ForumPost> posts = postMapper.selectAllPosts();
            
            if (posts != null && !posts.isEmpty()) {
                for (ForumPost post : posts) {
                    // 填充用户信息
                    User user = userMapper.selectById(post.getUserId());
                    post.setUser(user);
                    
                    // 获取评论
                    List<ForumComment> comments = getComments(post.getId());
                    post.setComments(comments);
                    
                    // 设置默认值
                    post.setTag("");
                    post.setImagesJson("");
                    post.setIsLiked(false);
                }
            }
            
            return posts;
        } catch (Exception e) {
            log.error("获取帖子列表失败", e);
            throw new RuntimeException("获取帖子列表失败");
        }
    }

    @Override
    public ForumPost getPost(Long id) {
        ForumPost post = postMapper.selectById(id);
        if (post != null) {
            // 设置用户信息
            User user = userMapper.selectById(post.getUserId());
            post.setUser(user);

            // 设置评论信息，使用 getComments 方法来确保评论包含用户信息
            post.setComments(getComments(post.getId()));

            // 设置点赞状态
            LambdaQueryWrapper<ForumLike> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ForumLike::getPostId, id)
                   .eq(ForumLike::getUserId, getCurrentUserId());
            post.setIsLiked(likeMapper.selectOne(wrapper) != null);
        }
        return post;
    }

    @Override
    @Transactional
    public ForumPost createPost(CreatePostDTO createPostDTO, Long userId) {
        ForumPost post = new ForumPost();
        post.setUserId(userId);
        post.setTitle(createPostDTO.getTitle());
        post.setContent(createPostDTO.getContent());
        post.setViewCount(0);
        post.setLikeCount(0);
        post.setCommentCount(0);
        
        postMapper.insert(post);

        // 设置用户信息
        User user = userMapper.selectById(userId);
        post.setUser(user);
        
        return post;
    }

    @Override
    @Transactional
    public ForumPost updatePost(Long id, CreatePostDTO updatePostDTO, Long userId) {
        // 检查帖子是否存在
        ForumPost post = postMapper.selectById(id);
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }
        
        // 检查是否有权限修改
        if (!post.getUserId().equals(userId)) {
            throw new RuntimeException("无权修改此帖子");
        }
        
        // 更新帖子内容
        post.setTitle(updatePostDTO.getTitle());
        post.setContent(updatePostDTO.getContent());
        if (updatePostDTO.getTag() != null) {
            post.setTag(updatePostDTO.getTag());
        }
        
        // 处理图片
        if (updatePostDTO.getImages() != null && !updatePostDTO.getImages().isEmpty()) {
            try {
                post.setImagesJson(objectMapper.writeValueAsString(updatePostDTO.getImages()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("图片处理失败", e);
            }
        }
        
        // 更新数据库
        postMapper.updateById(post);
        
        // 重新获取完整的帖子信息
        post = getPost(id);
        
        // 设置用户信息
        User user = userMapper.selectById(post.getUserId());
        post.setUser(user);
        
        return post;
    }

    @Override
    @Transactional
    public void deletePost(Long id, Long userId) {
        // 检查帖子是否存在
        ForumPost post = postMapper.selectById(id);
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }
        
        // 检查是否有权限删除
        if (!post.getUserId().equals(userId)) {
            throw new RuntimeException("无权删除此帖子");
        }
        
        // 删除相关的评论
        LambdaQueryWrapper<ForumComment> commentWrapper = new LambdaQueryWrapper<>();
        commentWrapper.eq(ForumComment::getPostId, id);
        commentMapper.delete(commentWrapper);
        
        // 删除相关的点赞
        LambdaQueryWrapper<ForumLike> likeWrapper = new LambdaQueryWrapper<>();
        likeWrapper.eq(ForumLike::getPostId, id);
        likeMapper.delete(likeWrapper);
        
        // 删除帖子
        postMapper.deleteById(id);
    }

    @Override
    public String uploadImage(String imageBase64) {
        // TODO: 实现图片上传逻辑
        return null;
    }

    @Override
    @Transactional
    public void addComment(Long postId, String content, Long userId) {
        try {
            // 创建新评论
            ForumComment comment = new ForumComment();
            comment.setPostId(postId);
            comment.setUserId(userId);
            comment.setContent(content);
            commentMapper.insert(comment);

            // 更新帖子的评论数
            ForumPost post = postMapper.selectById(postId);
            if (post != null) {
                // 获取实际的评论数
                LambdaQueryWrapper<ForumComment> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(ForumComment::getPostId, postId)
                      .eq(ForumComment::getDeleted, false);
                int commentCount = commentMapper.selectCount(wrapper).intValue();
                
                // 更新帖子的评论数
                post.setCommentCount(commentCount);
                postMapper.updateById(post);
            }

            // 设置评论的用户信息
            User user = userMapper.selectById(userId);
            comment.setUser(user);
        } catch (Exception e) {
            log.error("添加评论失败", e);
            throw new RuntimeException("添加评论失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteComment(Long postId, Long commentId, Long userId) {
        // 检查评论是否存在
        ForumComment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            throw new RuntimeException("评论不存在");
        }

        // 检查是否有权限删除（评论作者或帖子作者）
        ForumPost post = postMapper.selectById(postId);
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }

        if (!comment.getUserId().equals(userId) && !post.getUserId().equals(userId)) {
            throw new RuntimeException("无权删除此评论");
        }

        // 删除评论
        commentMapper.deleteById(commentId);

        // 更新帖子的评论数
        LambdaQueryWrapper<ForumComment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ForumComment::getPostId, postId)
              .eq(ForumComment::getDeleted, false);
        int commentCount = commentMapper.selectCount(wrapper).intValue();
        
        post.setCommentCount(commentCount);
        postMapper.updateById(post);
    }

    @Override
    @Transactional
    public ForumPost toggleLike(Long postId, Long userId) {
        LambdaQueryWrapper<ForumLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ForumLike::getPostId, postId)
               .eq(ForumLike::getUserId, userId);
        
        ForumLike like = likeMapper.selectOne(wrapper);
        ForumPost post = postMapper.selectById(postId);
        
        if (like == null) {
            // 添加点赞
            like = new ForumLike();
            like.setPostId(postId);
            like.setUserId(userId);
            likeMapper.insert(like);
            
            post.setLikeCount(post.getLikeCount() + 1);
            post.setIsLiked(true);
        } else {
            // 取消点赞
            likeMapper.delete(wrapper);
            post.setLikeCount(post.getLikeCount() - 1);
            post.setIsLiked(false);
        }
        
        postMapper.updateById(post);

        // 使用 getPost 方法获取完整的帖子信息，包括评论
        return getPost(postId);
    }

    @Override
    public List<ForumComment> getComments(Long postId) {
        try {
            // 使用自定义的查询方法
            List<ForumComment> comments = commentMapper.selectByPostId(postId);
            
            // 填充用户信息
            if (comments != null && !comments.isEmpty()) {
                for (ForumComment comment : comments) {
                    User user = userMapper.selectById(comment.getUserId());
                    comment.setUser(user);
                }
            }
            
            return comments;
        } catch (Exception e) {
            log.error("Error getting comments for post {}: {}", postId, e.getMessage());
            return new ArrayList<>();  // 返回空列表而不是抛出异常
        }
    }

    @Transactional
    public void updateAllPostCommentCounts() {
        try {
            List<ForumPost> posts = postMapper.selectList(null);
            for (ForumPost post : posts) {
                LambdaQueryWrapper<ForumComment> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(ForumComment::getPostId, post.getId())
                      .eq(ForumComment::getDeleted, false);
                int commentCount = commentMapper.selectCount(wrapper).intValue();
                
                post.setCommentCount(commentCount);
                postMapper.updateById(post);
            }
        } catch (Exception e) {
            log.error("更新帖子评论数失败", e);
            throw new RuntimeException("更新帖子评论数失败");
        }
    }

    @Override
    public List<ForumPost> getUserPosts(Long userId) {
        LambdaQueryWrapper<ForumPost> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ForumPost::getUserId, userId)
              .eq(ForumPost::getDeleted, false)
              .orderByDesc(ForumPost::getCreatedTime);
              
        List<ForumPost> posts = postMapper.selectList(wrapper);
        
        // 填充用户信息和评论
        if (posts != null && !posts.isEmpty()) {
            for (ForumPost post : posts) {
                User user = userMapper.selectById(post.getUserId());
                post.setUser(user);
                post.setComments(getComments(post.getId()));
            }
        }
        
        return posts;
    }

    private Long getCurrentUserId() {
        // 通过 ApplicationContext 获取 UserService
        UserService userService = applicationContext.getBean(UserService.class);
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("用户未登录");
        }
        return currentUser.getId();
    }
} 