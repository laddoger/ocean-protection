package com.ocean.protection.dto;

import com.ocean.protection.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDTO {
    private String token;
    private User user;

    public static LoginResponseDTO of(String token, User user) {
        LoginResponseDTO dto = new LoginResponseDTO();
        dto.token = token;
        dto.user = user;
        return dto;
    }
} 