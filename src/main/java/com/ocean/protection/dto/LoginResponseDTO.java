package com.ocean.protection.dto;

import com.ocean.protection.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDTO {
    private String token;
    private User user;
} 