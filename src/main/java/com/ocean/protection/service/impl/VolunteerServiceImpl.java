package com.ocean.protection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ocean.protection.dto.*;
import com.ocean.protection.entity.*;
import com.ocean.protection.mapper.*;
import com.ocean.protection.service.VolunteerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class VolunteerServiceImpl implements VolunteerService {

    private final VolunteerOrganizationMapper organizationMapper;
    private final VolunteerActivityMapper activityMapper;
    private final OrganizationMemberMapper memberMapper;
    private final ActivityParticipantMapper participantMapper;
    private final UserMapper userMapper;

    @Override
    public List<VolunteerOrganization> getAllOrganizations() {
        return organizationMapper.selectList(new LambdaQueryWrapper<>());
    }

    @Override
    public List<VolunteerOrganization> searchOrganizations(String keyword) {
        return organizationMapper.selectList(
            new LambdaQueryWrapper<VolunteerOrganization>()
                .like(VolunteerOrganization::getName, keyword)
                .or()
                .like(VolunteerOrganization::getDescription, keyword)
        );
    }

    @Override
    @Transactional
    public VolunteerOrganization createOrganization(VolunteerOrganization organization) {
        organizationMapper.insert(organization);
        
        // 创建者自动成为成员
        OrganizationMember member = new OrganizationMember();
        member.setOrganizationId(organization.getId());
        member.setUserId(organization.getFounderId());
        member.setRole("FOUNDER");
        memberMapper.insert(member);
        
        return organization;
    }

    @Override
    @Transactional
    public void deleteOrganization(Long organizationId, Long userId) {
        VolunteerOrganization organization = organizationMapper.selectById(organizationId);
        if (organization == null || !organization.getFounderId().equals(userId)) {
            throw new RuntimeException("无权删除该组织");
        }
        
        // 逻辑删除组织
        organizationMapper.deleteById(organizationId);
        
        // 删除相关成员记录
        memberMapper.delete(
            new LambdaQueryWrapper<OrganizationMember>()
                .eq(OrganizationMember::getOrganizationId, organizationId)
        );
    }

    @Override
    @Transactional
    public void joinOrganization(Long organizationId, Long userId) {
        // 检查是否已是成员
        if (isMember(organizationId, userId)) {
            throw new RuntimeException("已经是该组织成员");
        }
        
        OrganizationMember member = new OrganizationMember();
        member.setOrganizationId(organizationId);
        member.setUserId(userId);
        member.setRole("MEMBER");
        memberMapper.insert(member);
        
        // 更新成员数量
        VolunteerOrganization organization = organizationMapper.selectById(organizationId);
        organization.setMemberCount(organization.getMemberCount() + 1);
        organizationMapper.updateById(organization);
    }

    @Override
    @Transactional
    public void leaveOrganization(Long organizationId, Long userId) {
        // 创建者不能退出
        OrganizationMember member = memberMapper.selectOne(
            new LambdaQueryWrapper<OrganizationMember>()
                .eq(OrganizationMember::getOrganizationId, organizationId)
                .eq(OrganizationMember::getUserId, userId)
        );
        
        if (member == null) {
            throw new RuntimeException("不是该组织成员");
        }
        
        if ("FOUNDER".equals(member.getRole())) {
            throw new RuntimeException("创建者不能退出组织");
        }
        
        memberMapper.deleteById(member.getId());
        
        // 更新成员数量
        VolunteerOrganization organization = organizationMapper.selectById(organizationId);
        organization.setMemberCount(organization.getMemberCount() - 1);
        organizationMapper.updateById(organization);
    }

    @Override
    public List<VolunteerActivity> getOngoingActivities() {
        return activityMapper.selectList(
            new LambdaQueryWrapper<VolunteerActivity>()
                .eq(VolunteerActivity::getStatus, "ONGOING")
        );
    }

    @Override
    public List<VolunteerActivity> getFinishedActivities() {
        return activityMapper.selectList(
            new LambdaQueryWrapper<VolunteerActivity>()
                .eq(VolunteerActivity::getStatus, "FINISHED")
        );
    }

    @Override
    public List<VolunteerActivity> searchActivities(String keyword) {
        return activityMapper.selectList(
            new LambdaQueryWrapper<VolunteerActivity>()
                .like(VolunteerActivity::getTitle, keyword)
                .or()
                .like(VolunteerActivity::getDescription, keyword)
        );
    }

    @Override
    public VolunteerActivity getActivityDetail(Long activityId) {
        VolunteerActivity activity = activityMapper.selectById(activityId);
        if (activity != null) {
            // 加载组织信息
            activity.setOrganization(organizationMapper.selectById(activity.getOrganizationId()));
        }
        return activity;
    }

    @Override
    @Transactional
    public void joinActivity(Long activityId, Long userId) {
        VolunteerActivity activity = activityMapper.selectById(activityId);
        if (activity == null) {
            throw new RuntimeException("活动不存在");
        }
        
        if ("FINISHED".equals(activity.getStatus())) {
            throw new RuntimeException("活动已结束");
        }
        
        if (activity.getParticipantCount() >= activity.getMaxParticipants()) {
            throw new RuntimeException("活动人数已满");
        }
        
        // 检查是否已参加
        if (isParticipant(activityId, userId)) {
            throw new RuntimeException("已参加该活动");
        }
        
        ActivityParticipant participant = new ActivityParticipant();
        participant.setActivityId(activityId);
        participant.setUserId(userId);
        participantMapper.insert(participant);
        
        // 更新参与人数
        activity.setParticipantCount(activity.getParticipantCount() + 1);
        activityMapper.updateById(activity);
    }

    @Override
    @Transactional
    public void leaveActivity(Long activityId, Long userId) {
        VolunteerActivity activity = activityMapper.selectById(activityId);
        if (activity == null) {
            throw new RuntimeException("活动不存在");
        }
        
        if ("FINISHED".equals(activity.getStatus())) {
            throw new RuntimeException("活动已结束");
        }
        
        // 检查是否已参加
        ActivityParticipant participant = participantMapper.selectOne(
            new LambdaQueryWrapper<ActivityParticipant>()
                .eq(ActivityParticipant::getActivityId, activityId)
                .eq(ActivityParticipant::getUserId, userId)
        );
        
        if (participant == null) {
            throw new RuntimeException("未参加该活动");
        }
        
        participantMapper.deleteById(participant.getId());
        
        // 更新参与人数
        activity.setParticipantCount(activity.getParticipantCount() - 1);
        activityMapper.updateById(activity);
    }

    @Override
    @Transactional
    public void disbandOrganization(Long organizationId, Long userId) {
        // 获取组织信息
        VolunteerOrganization organization = organizationMapper.selectById(organizationId);
        if (organization == null) {
            throw new RuntimeException("组织不存在");
        }

        // 验证当前用户是否是组织创建者
        if (!organization.getFounderId().equals(userId)) {
            throw new RuntimeException("只有组织创建者可以解散组织");
        }

        try {
            // 删除组织成员关系
            LambdaQueryWrapper<OrganizationMember> memberWrapper = new LambdaQueryWrapper<>();
            memberWrapper.eq(OrganizationMember::getOrganizationId, organizationId);
            organizationMemberMapper.delete(memberWrapper);

            // 删除组织相关的活动参与记录
            LambdaQueryWrapper<ActivityParticipant> participantWrapper = new LambdaQueryWrapper<>();
            participantWrapper.eq(ActivityParticipant::getOrganizationId, organizationId);
            activityParticipantMapper.delete(participantWrapper);

            // 删除组织的活动
            LambdaQueryWrapper<VolunteerActivity> activityWrapper = new LambdaQueryWrapper<>();
            activityWrapper.eq(VolunteerActivity::getOrganizationId, organizationId);
            activityMapper.delete(activityWrapper);

            // 最后删除组织
            organizationMapper.deleteById(organizationId);

            log.info("Organization {} disbanded by user {}", organizationId, userId);
        } catch (Exception e) {
            log.error("Error disbanding organization {}: {}", organizationId, e.getMessage());
            throw new RuntimeException("解散组织失败：" + e.getMessage());
        }
    }

    @Override
    public List<VolunteerOrganization> getUserOrganizations(Long userId) {
        log.info("Getting organizations for user: {}", userId);
        
        // 获取用户加入的组织ID列表
        List<Long> organizationIds = memberMapper.selectList(
            new LambdaQueryWrapper<OrganizationMember>()
                .eq(OrganizationMember::getUserId, userId)
                .eq(OrganizationMember::getDeleted, false)
        ).stream().map(OrganizationMember::getOrganizationId).collect(Collectors.toList());
        
        log.info("Found organization IDs: {}", organizationIds);
        
        if (organizationIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 获取组织详细信息
        List<VolunteerOrganization> organizations = organizationMapper.selectList(
            new LambdaQueryWrapper<VolunteerOrganization>()
                .in(VolunteerOrganization::getId, organizationIds)
                .eq(VolunteerOrganization::getDeleted, false)
        );
        
        // 填充创建者信息
        for (VolunteerOrganization org : organizations) {
            org.setFounder(userMapper.selectById(org.getFounderId()));
        }
        
        log.info("Found organizations: {}", organizations.size());
        return organizations;
    }

    @Override
    public List<VolunteerActivity> getUserActivities(Long userId) {
        log.info("Getting activities for user: {}", userId);
        
        // 获取用户参加的活动ID列表
        List<Long> activityIds = participantMapper.selectList(
            new LambdaQueryWrapper<ActivityParticipant>()
                .eq(ActivityParticipant::getUserId, userId)
                .eq(ActivityParticipant::getDeleted, false)
        ).stream().map(ActivityParticipant::getActivityId).collect(Collectors.toList());
        
        log.info("Found activity IDs: {}", activityIds);
        
        if (activityIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 获取活动详细信息
        List<VolunteerActivity> activities = activityMapper.selectList(
            new LambdaQueryWrapper<VolunteerActivity>()
                .in(VolunteerActivity::getId, activityIds)
                .eq(VolunteerActivity::getDeleted, false)
        );
        
        log.info("Found activities: {}", activities.size());
        
        // 填充组织信息
        for (VolunteerActivity activity : activities) {
            activity.setOrganization(
                organizationMapper.selectById(activity.getOrganizationId())
            );
        }
        
        return activities;
    }

    private boolean isMember(Long organizationId, Long userId) {
        return memberMapper.selectCount(
            new LambdaQueryWrapper<OrganizationMember>()
                .eq(OrganizationMember::getOrganizationId, organizationId)
                .eq(OrganizationMember::getUserId, userId)
        ) > 0;
    }

    private boolean isParticipant(Long activityId, Long userId) {
        return participantMapper.selectCount(
            new LambdaQueryWrapper<ActivityParticipant>()
                .eq(ActivityParticipant::getActivityId, activityId)
                .eq(ActivityParticipant::getUserId, userId)
        ) > 0;
    }
} 