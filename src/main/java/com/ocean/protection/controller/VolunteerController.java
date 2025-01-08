package com.ocean.protection.controller;

import com.ocean.protection.common.result.Result;
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
            return Result.success(volunteerService.getAllOrganizations());
        } catch (Exception e) {
            log.error("获取组织列表失败", e);
            return Result.error("获取组织列表失败");
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

    // 活动相关接口
    @GetMapping("/activities/ongoing")
    public Result<List<VolunteerActivity>> getOngoingActivities() {
        try {
            return Result.success(volunteerService.getOngoingActivities());
        } catch (Exception e) {
            log.error("获取进行中活动失败", e);
            return Result.error("获取进行中活动失败");
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
        return Result.success(volunteerService.getActivityDetail(id));
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
} 