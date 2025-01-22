package com.ocean.protection.service;

public interface OrganizationService {
    boolean isMember(Long organizationId, Long userId);
    void removeMember(Long organizationId, Long userId);
    void addMember(Long organizationId, Long userId);
} 