package com.ocean.protection.controller;

import com.ocean.protection.common.result.Result;
import com.ocean.protection.dto.*;
import com.ocean.protection.entity.User;
import com.ocean.protection.entity.ForumPost;
import com.ocean.protection.entity.VolunteerOrganization;
import com.ocean.protection.entity.VolunteerActivity;
import com.ocean.protection.service.UserService;
import com.ocean.protection.service.ForumService;
import com.ocean.protection.service.VolunteerService;
import com.ocean.protection.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ForumService forumService;
    private final VolunteerService volunteerService;
    private final FileService fileService;

    @PostMapping("/register")
    public Result<User> register(@RequestBody RegisterDTO registerDTO) {
        return Result.success(userService.register(registerDTO));
    }

    @PostMapping("/login")
    public Result<LoginResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        return Result.success(userService.login(loginDTO));
    }

    @PutMapping("/user/info")
    public Result<User> updateUserInfo(@RequestBody Map<String, Object> params) {
        try {
            Long userId = getCurrentUserId();
            String gender = (String) params.get("gender");
            Integer age = params.get("age") != null ? 
                Integer.parseInt(params.get("age").toString()) : null;
            String address = (String) params.get("address");
            
            User updatedUser = userService.updateUserInfo(userId, gender, age, address);
            return Result.success(updatedUser);
        } catch (Exception e) {
            return Result.error("更新用户信息失败: " + e.getMessage());
        }
    }

    @GetMapping("/user/info")
    public Result<User> getUserInfo() {
        try {
            User user = userService.getCurrentUser();
            return Result.success(user);
        } catch (Exception e) {
            return Result.error("获取用户信息失败: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public Result<String> logout() {
        // 简单返回成功即可，因为是无状态的
        return Result.success("退出成功");
    }

    @GetMapping("/current")
    public Result<User> getCurrentUser() {
        return Result.success(userService.getCurrentUser());
    }

    @PostMapping("/avatar")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        try {
            log.info("开始上传用户头像，文件名: {}, 大小: {}", file.getOriginalFilename(), file.getSize());
            
            // 验证文件是否为空
            if (file.isEmpty()) {
                return Result.error("请选择要上传的图片");
            }
            
            // 验证文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                log.warn("不支持的文件类型: {}", contentType);
                return Result.error("只能上传图片文件");
            }
            
            // 验证文件大小（最大2MB）
            if (file.getSize() > 2 * 1024 * 1024) {
                log.warn("文件过大: {} bytes", file.getSize());
                return Result.error("图片大小不能超过2MB");
            }
            
            // 上传文件
            String avatarUrl = fileService.uploadFile(file, "avatars");
            log.info("文件上传成功，访问URL: {}", avatarUrl);
            
            // 更新用户头像
            userService.updateAvatar(avatarUrl);
            
            return Result.success(avatarUrl);
            
        } catch (Exception e) {
            log.error("上传头像失败", e);
            return Result.error("上传头像失败: " + e.getMessage());
        }
    }

    @GetMapping("/profile")
    public Result<UserProfileDTO> getProfile() {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return Result.error("用户未登录");
        }
        return Result.success(userService.getUserProfile(currentUser.getId()));
    }

    @PutMapping("/profile")
    public Result<Void> updateProfile(@RequestBody UpdateProfileDTO updateProfileDTO) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return Result.error("用户未登录");
        }
        userService.updateProfile(currentUser.getId(), updateProfileDTO);
        return Result.success(null);
    }

    @GetMapping("/user/posts")
    public Result<List<ForumPost>> getUserPosts() {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return Result.error("用户未登录");
        }
        return Result.success(forumService.getUserPosts(currentUser.getId()));
    }

    @GetMapping("/user/organizations")
    public Result<List<VolunteerOrganization>> getUserOrganizations() {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return Result.error("用户未登录");
        }
        return Result.success(volunteerService.getUserOrganizations(currentUser.getId()));
    }

    @GetMapping("/user/activities")
    public Result<List<VolunteerActivity>> getUserActivities() {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return Result.error("用户未登录");
        }
        return Result.success(volunteerService.getUserActivities(currentUser.getId()));
    }

    private Long getCurrentUserId() {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("用户未登录");
        }
        return currentUser.getId();
    }
} 