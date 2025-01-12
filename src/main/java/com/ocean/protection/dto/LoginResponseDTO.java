package com.ocean.protection.dto;

import com.ocean.protection.entity.User;
import lombok.Data;

@Data
public class LoginResponseDTO {
    private String token;
    private User user;
} 