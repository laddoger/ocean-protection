package com.ocean.protection.controller;

import com.ocean.protection.common.result.Result;
import com.ocean.protection.common.utils.SecurityUtils;
import com.ocean.protection.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/organizations")
public class OrganizationController {
    
    private final OrganizationService organizationService;

    @GetMapping("/{organizationId}/membership")
    public Result<Boolean> checkMembership(@PathVariable Long organizationId) {
        try {
            Long userId = SecurityUtils.getLoginUserId();
            boolean isMember = organizationService.isMember(organizationId, userId);
            return Result.success(isMember);
        } catch (Exception e) {
            log.error("检查组织成员状态失败", e);
            return Result.error("检查组织成员状态失败: " + e.getMessage());
        }
    }

    @PostMapping("/{organizationId}/members")
    public Result<Void> joinOrganization(@PathVariable Long organizationId) {
        try {
            Long userId = SecurityUtils.getLoginUserId();
            organizationService.addMember(organizationId, userId);
            return Result.success();
        } catch (ServiceException e) {
            log.warn("加入组织失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("加入组织失败", e);
            return Result.error("加入组织失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{organizationId}/members")
    public Result<Void> quitOrganization(@PathVariable Long organizationId) {
        try {
            Long userId = SecurityUtils.getLoginUserId();
            organizationService.removeMember(organizationId, userId);
            return Result.success();
        } catch (ServiceException e) {
            log.warn("退出组织失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("退出组织失败", e);
            return Result.error("退出组织失败: " + e.getMessage());
        }
    }
} 