package com.ocean.protection.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "123456";
        String encodedPassword = encoder.encode(password);
        System.out.println("Original password: " + password);
        System.out.println("Encoded password: " + encodedPassword);
        
        // 验证密码
        boolean matches = encoder.matches(password, encodedPassword);
        System.out.println("Password matches: " + matches);
        
        // 验证现有密码
        String existingHash = "$2a$10$N.ZOn9G6w6e2dl2WDMB2yOtbz/E9I.OzwQPI9Z7iHQKz/BBQYry1G";
        boolean existingMatches = encoder.matches(password, existingHash);
        System.out.println("Existing password matches: " + existingMatches);
    }
} 