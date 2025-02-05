package com.ocean.protection.controller;

import com.ocean.protection.common.result.Result;
import com.ocean.protection.common.utils.SecurityUtils;
import com.ocean.protection.dto.CreateOrganizationDTO;
import com.ocean.protection.entity.*;
import com.ocean.protection.service.UserService;
import com.ocean.protection.service.VolunteerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/volunteer")
@RequiredArgsConstructor
public class VolunteerController {

    private final VolunteerService volunteerService;
    private final UserService userService;

    // 组织相关接口
    @GetMapping("/organizations")
    public Result<List<VolunteerOrganization>> getOrganizations() {
        try {
            List<VolunteerOrganization> organizations = volunteerService.getAllOrganizations();
            log.info("成功获取组织列表，数量: {}", organizations.size());
            return Result.success(organizations);
        } catch (Exception e) {
            log.error("获取组织列表失败，错误信息: {}", e.getMessage(), e);
            return Result.error("获取组织列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/organizations/search")
    public Result<List<VolunteerOrganization>> searchOrganizations(@RequestParam String keyword) {
        return Result.success(volunteerService.searchOrganizations(keyword));
    }

    @PostMapping("/organizations")
    public Result<VolunteerOrganization> createOrganization(@RequestBody CreateOrganizationDTO dto) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return Result.error("用户未登录");
        }

        VolunteerOrganization organization = new VolunteerOrganization();
        organization.setName(dto.getName());
        organization.setDescription(dto.getDescription());
        organization.setFounderId(currentUser.getId());
        organization.setMemberCount(1); // 创建者算一个成员

        return Result.success(volunteerService.createOrganization(organization));
    }

    @DeleteMapping("/organizations/{id}")
    public Result<Void> deleteOrganization(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return Result.error("用户未登录");
        }

        try {
            volunteerService.deleteOrganization(id, currentUser.getId());
            return Result.success(null);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/organizations/{id}/join")
    public Result<Void> joinOrganization(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return Result.error("用户未登录");
        }

        try {
            volunteerService.joinOrganization(id, currentUser.getId());
            return Result.success(null);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/organizations/{id}/leave")
    public Result<Void> leaveOrganization(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return Result.error("用户未登录");
        }

        try {
            volunteerService.leaveOrganization(id, currentUser.getId());
            return Result.success(null);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/organizations/{id}/disband")
    public Result<Void> disbandOrganization(@PathVariable Long id) {
        try {
            User currentUser = userService.getCurrentUser();
            if (currentUser == null) {
                return Result.error("用户未登录");
            }
            volunteerService.disbandOrganization(id, currentUser.getId());
            return Result.success(null);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/organizations/{organizationId}/membership")
    public Result<Boolean> checkMembership(@PathVariable Long organizationId) {
        try {
            Long userId = SecurityUtils.getLoginUserId();
            boolean isMember = volunteerService.isMember(organizationId, userId);
            log.info("检查用户 {} 是否是组织 {} 的成员: {}", userId, organizationId, isMember);
            return Result.success(isMember);
        } catch (Exception e) {
            log.error("检查组织成员状态失败", e);
            return Result.error("检查组织成员状态失败: " + e.getMessage());
        }
    }

    // 活动相关接口
    @GetMapping("/activities/ongoing")
    public Result<List<VolunteerActivity>> getOngoingActivities() {
        try {
            log.info("开始获取进行中的活动");
            List<VolunteerActivity> activities = volunteerService.getOngoingActivities();
            log.info("成功获取进行中的活动，数量: {}", activities.size());
            return Result.success(activities);
        } catch (Exception e) {
            log.error("获取进行中活动失败: {}", e.getMessage(), e);
            return Result.error("获取进行中活动失败: " + e.getMessage());
        }
    }

    @GetMapping("/activities/finished")
    public Result<List<VolunteerActivity>> getFinishedActivities() {
        return Result.success(volunteerService.getFinishedActivities());
    }

    @GetMapping("/activities/search")
    public Result<List<VolunteerActivity>> searchActivities(@RequestParam String keyword) {
        return Result.success(volunteerService.searchActivities(keyword));
    }

    @GetMapping("/activities/{id}")
    public Result<VolunteerActivity> getActivityDetail(@PathVariable Long id) {
        try {
            VolunteerActivity activity = volunteerService.getActivityDetail(id);
            if (activity != null) {
                Long userId = SecurityUtils.getLoginUserId();
                if (userId != null) {
                    activity.setIsParticipant(volunteerService.checkActivityParticipation(id, userId));
                }
            }
            return Result.success(activity);
        } catch (Exception e) {
            log.error("获取活动详情失败", e);
            return Result.error("获取活动详情失败: " + e.getMessage());
        }
    }

    @PostMapping("/activities/{id}/join")
    public Result<Void> joinActivity(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return Result.error("用户未登录");
        }

        try {
            volunteerService.joinActivity(id, currentUser.getId());
            return Result.success(null);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/activities/{id}/leave")
    public Result<Void> leaveActivity(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return Result.error("用户未登录");
        }

        try {
            volunteerService.leaveActivity(id, currentUser.getId());
            return Result.success(null);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/activities/{activityId}/participation")
    public Result<Boolean> checkActivityParticipation(@PathVariable Long activityId) {
        try {
            log.info("检查活动参与状态，活动ID: {}", activityId);
            Long userId = SecurityUtils.getLoginUserId();
            log.info("当前用户ID: {}", userId);
            
            boolean isParticipant = volunteerService.checkActivityParticipation(activityId, userId);
            log.info("用户 {} 是否参与活动 {}: {}", userId, activityId, isParticipant);
            
            return Result.success(isParticipant);
        } catch (Exception e) {
            log.error("检查活动参与状态失败，活动ID: {}", activityId, e);
            return Result.error("检查活动参与状态失败: " + e.getMessage());
        }
    }
} 