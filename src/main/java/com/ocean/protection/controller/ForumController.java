package com.ocean.protection.controller;

import com.ocean.protection.common.result.Result;
import com.ocean.protection.dto.*;
import com.ocean.protection.entity.*;
import com.ocean.protection.service.ForumService;
import com.ocean.protection.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/forum")
@RequiredArgsConstructor
public class ForumController {

    private final ForumService forumService;
    private final UserService userService;
    
    @GetMapping("/posts")
    public Result<List<ForumPost>> getPosts() {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return Result.error("用户未登录");
        }
        return Result.success(forumService.getPosts(currentUser.getId()));
    }

    @PostMapping("/posts")
    public Result<ForumPost> createPost(@RequestBody CreatePostDTO createPostDTO) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return Result.error("用户未登录");
        }
        return Result.success(forumService.createPost(createPostDTO, currentUser.getId()));
    }

    @PutMapping("/posts/{id}")
    public Result<ForumPost> updatePost(@PathVariable Long id, @RequestBody CreatePostDTO updatePostDTO) {
        try {
            User currentUser = userService.getCurrentUser();
            if (currentUser == null) {
                return Result.error("用户未登录");
            }
            ForumPost updatedPost = forumService.updatePost(id, updatePostDTO, currentUser.getId());
            return Result.success(updatedPost);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/posts/{id}")
    public Result<Void> deletePost(@PathVariable Long id) {
        try {
            User currentUser = userService.getCurrentUser();
            if (currentUser == null) {
                return Result.error("用户未登录");
            }
            forumService.deletePost(id, currentUser.getId());
            return Result.success(null);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/posts/{postId}/comments")
    public Result<List<ForumComment>> getComments(@PathVariable Long postId) {
        return Result.success(forumService.getComments(postId));
    }

    @PostMapping("/posts/{postId}/comments/add")
    public Result<List<ForumComment>> addComment(
            @PathVariable Long postId,
            @RequestBody CommentDTO commentDTO) {
        try {
            User currentUser = userService.getCurrentUser();
            if (currentUser == null) {
                return Result.error("用户未登录");
            }
            forumService.addComment(postId, commentDTO.getContent(), currentUser.getId());
            return Result.success(forumService.getComments(postId));
        } catch (Exception e) {
            return Result.error("添加评论失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public Result<Void> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return Result.error("用户未登录");
        }
        forumService.deleteComment(postId, commentId, currentUser.getId());
        return Result.success(null);
    }

    @PostMapping("/posts/{postId}/like")
    public Result<ForumPost> toggleLike(@PathVariable Long postId) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return Result.error("用户未登录");
        }
        return Result.success(forumService.toggleLike(postId, currentUser.getId()));
    }

    @PostMapping("/upload")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
        // TODO: 实现文件上传逻辑
        return Result.success("临时URL");
    }
} 