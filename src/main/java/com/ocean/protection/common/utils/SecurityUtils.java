package com.ocean.protection.common.utils;

import com.ocean.protection.common.exception.ServiceException;
import com.ocean.protection.entity.User;
import com.ocean.protection.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityUtils {
    
    private final UserService userService;
    
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            log.error("获取认证信息失败：authentication 为 null");
            throw new ServiceException("用户未登录");
        }
        
        Object principal = authentication.getPrincipal();
        log.info("当前用户 principal 类型: {}, 值: {}", 
                principal != null ? principal.getClass().getName() : "null", 
                principal);
        
        if (principal == null) {
            throw new ServiceException("用户未登录");
        }
        
        // 如果 principal 是 User 对象
        if (principal instanceof User) {
            return ((User) principal).getId();
        }
        
        // 如果 principal 是用户名，通过用户名查找用户
        String username = principal.toString();
        User user = userService.getUserByUsername(username);
        if (user == null) {
            log.error("未找到用户: {}", username);
            throw new ServiceException("用户不存在");
        }
        
        return user.getId();
    }
    
    public static SecurityUtils getInstance() {
        return SpringContextHolder.getBean(SecurityUtils.class);
    }
    
    public static Long getLoginUserId() {
        return getInstance().getCurrentUserId();
    }
} 