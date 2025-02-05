package com.ocean.protection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ocean.protection.entity.Organization;

public interface OrganizationService extends IService<Organization> {
    boolean isMember(Long organizationId, Long userId);
    void addMember(Long organizationId, Long userId);
    void removeMember(Long organizationId, Long userId);
} 