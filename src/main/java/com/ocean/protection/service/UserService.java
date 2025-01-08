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
} 