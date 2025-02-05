package com.ocean.protection.service;

import com.ocean.protection.dto.LoginDTO;
import com.ocean.protection.dto.LoginResponseDTO;
import com.ocean.protection.dto.RegisterDTO;
import com.ocean.protection.dto.UserProfileDTO;
import com.ocean.protection.dto.UpdateProfileDTO;
import com.ocean.protection.entity.User;

public interface UserService {
    LoginResponseDTO login(LoginDTO loginDTO);
    User register(RegisterDTO registerDTO);
    User getCurrentUser();
    UserProfileDTO getUserProfile(Long userId);
    void updateProfile(Long userId, UpdateProfileDTO updateProfileDTO);
    /**
     * 更新用户信息
     * @param userId 用户ID
     * @param gender 性别
     * @param age 年龄
     * @param address 地址
     * @return 更新后的用户信息
     */
    User updateUserInfo(Long userId, String gender, Integer age, String address);
    User getUserByUsername(String username);
    User getUserById(Long id);
    User login(String username, String password);
    void updateAvatar(String avatarUrl);
} 