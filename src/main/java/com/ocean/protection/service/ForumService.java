package com.ocean.protection.service;

import com.ocean.protection.dto.CreatePostDTO;
import com.ocean.protection.entity.ForumPost;
import com.ocean.protection.entity.ForumComment;
import java.util.List;

public interface ForumService {
    List<ForumPost> getPosts(Long currentUserId);
    ForumPost getPost(Long id);
    ForumPost createPost(CreatePostDTO createPostDTO, Long userId);
    ForumPost updatePost(Long id, CreatePostDTO updatePostDTO, Long userId);
    void deletePost(Long id, Long userId);
    String uploadImage(String imageBase64);
    void addComment(Long postId, String content, Long userId);
    void deleteComment(Long postId, Long commentId, Long userId);
    ForumPost toggleLike(Long postId, Long userId);
    List<ForumComment> getComments(Long postId);
} 