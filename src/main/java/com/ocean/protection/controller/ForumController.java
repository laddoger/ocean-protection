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
    public Result<ForumPost> createPost(@RequestBody(required = false) CreatePostDTO createPostDTO,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            User currentUser = userService.getCurrentUser();
            if (currentUser == null) {
                return Result.error("用户未登录");
            }

            // 如果是 JSON 请求，使用 createPostDTO
            if (createPostDTO != null) {
                log.info("收到 JSON 格式请求：title={}, content={}", createPostDTO.getTitle(), createPostDTO.getContent());
                return Result.success(forumService.createPost(createPostDTO, currentUser.getId()));
            }

            // 如果是 form-data 请求，使用单独的参数
            if (title == null || content == null) {
                return Result.error("标题和内容不能为空");
            }

            log.info("收到 form-data 格式请求：title={}, content={}", title, content);
            CreatePostDTO dto = new CreatePostDTO();
            dto.setTitle(title);
            dto.setContent(content);
            dto.setTag(tag);
            dto.setImage(image);

            ForumPost post = forumService.createPost(dto, currentUser.getId());
            return Result.success(post);
        } catch (Exception e) {
            log.error("创建帖子失败", e);
            return Result.error("创建帖子失败: " + e.getMessage());
        }
    }

    @PutMapping("/posts/{id}")
    public Result<ForumPost> updatePost(@PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            log.info("开始更新帖子，ID: {}, 标题: {}, 内容长度: {}, 标签: {}", 
                    id, title, content.length(), tag);
            
            // 添加详细的图片信息日志
            if (image != null) {
                log.info("收到图片文件: 名称={}, 大小={}, 类型={}", 
                    image.getOriginalFilename(),
                    image.getSize(),
                    image.getContentType());
            } else {
                log.info("未收到图片文件");
            }
            
            User currentUser = userService.getCurrentUser();
            if (currentUser == null) {
                return Result.error("用户未登录");
            }

            CreatePostDTO dto = new CreatePostDTO();
            dto.setTitle(title);
            dto.setContent(content);
            dto.setTag(tag);
            dto.setImage(image);

            ForumPost post = forumService.updatePost(id, dto, currentUser.getId());
            log.info("帖子更新成功，ID: {}", post.getId());
            return Result.success(post);
        } catch (Exception e) {
            log.error("更新帖子失败，ID: {}, 错误: {}", id, e.getMessage(), e);
            return Result.error("更新帖子失败: " + e.getMessage());
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