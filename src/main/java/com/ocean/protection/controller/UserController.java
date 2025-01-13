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
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ForumService forumService;
    private final VolunteerService volunteerService;

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
    public Result<String> updateAvatar(@RequestParam("file") MultipartFile file) {
        // TODO: 实现头像上传逻辑
        return Result.success("临时头像URL");
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