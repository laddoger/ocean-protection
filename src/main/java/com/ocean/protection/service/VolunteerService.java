package com.ocean.protection.service;

import com.ocean.protection.entity.VolunteerActivity;
import com.ocean.protection.entity.VolunteerOrganization;
import java.util.List;

public interface VolunteerService {
    // 组织相关
    List<VolunteerOrganization> getAllOrganizations();
    List<VolunteerOrganization> searchOrganizations(String keyword);
    VolunteerOrganization createOrganization(VolunteerOrganization organization);
    void deleteOrganization(Long organizationId, Long userId);
    void joinOrganization(Long organizationId, Long userId);
    void leaveOrganization(Long organizationId, Long userId);
    void disbandOrganization(Long organizationId, Long userId);
    
    // 活动相关
    List<VolunteerActivity> getOngoingActivities();
    List<VolunteerActivity> getFinishedActivities();
    List<VolunteerActivity> searchActivities(String keyword);
    VolunteerActivity getActivityDetail(Long activityId);
    void joinActivity(Long activityId, Long userId);
    void leaveActivity(Long activityId, Long userId);
} 