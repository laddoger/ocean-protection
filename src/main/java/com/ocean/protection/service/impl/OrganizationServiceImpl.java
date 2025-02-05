package com.ocean.protection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ocean.protection.entity.Organization;
import com.ocean.protection.entity.OrganizationMember;
import com.ocean.protection.exception.ServiceException;
import com.ocean.protection.mapper.OrganizationMapper;
import com.ocean.protection.mapper.OrganizationMemberMapper;
import com.ocean.protection.service.OrganizationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements OrganizationService {
    
    private final OrganizationMemberMapper memberMapper;
    
    @Autowired
    public OrganizationServiceImpl(OrganizationMemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    @Override
    public boolean isMember(Long organizationId, Long userId) {
        if (organizationId == null || userId == null) {
            return false;
        }
        
        LambdaQueryWrapper<OrganizationMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrganizationMember::getOrganizationId, organizationId)
               .eq(OrganizationMember::getUserId, userId)
               .eq(OrganizationMember::getDeleted, false);
                
        return memberMapper.selectCount(wrapper) > 0;
    }

    @Override
    public void removeMember(Long organizationId, Long userId) {
        if (organizationId == null || userId == null) {
            throw new ServiceException("参数不能为空");
        }
        
        // 检查是否是成员
        if (!isMember(organizationId, userId)) {
            throw new ServiceException("用户不是该组织成员");
        }
        
        LambdaQueryWrapper<OrganizationMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrganizationMember::getOrganizationId, organizationId)
               .eq(OrganizationMember::getUserId, userId);
                
        memberMapper.delete(wrapper);
        
        // 更新组织成员数量
        Organization org = getById(organizationId);
        if (org != null && org.getMemberCount() > 0) {
            org.setMemberCount(org.getMemberCount() - 1);
            updateById(org);
        }
    }

    @Override
    @Transactional
    public void addMember(Long organizationId, Long userId) {
        if (organizationId == null || userId == null) {
            throw new ServiceException("参数不能为空");
        }
        
        // 先检查用户是否已经是成员
        if (isMember(organizationId, userId)) {
            throw new ServiceException("用户已经是该组织成员");
        }
        
        // 检查组织是否存在
        Organization org = getById(organizationId);
        if (org == null || org.getDeleted()) {
            throw new ServiceException("组织不存在");
        }
        
        // 添加成员
        OrganizationMember member = new OrganizationMember();
        member.setOrganizationId(organizationId);
        member.setUserId(userId);
        member.setRole("MEMBER");
        member.setDeleted(false);
        
        try {
            memberMapper.insert(member);
            
            // 更新组织成员数量
            org.setMemberCount(org.getMemberCount() + 1);
            updateById(org);
        } catch (DuplicateKeyException e) {
            throw new ServiceException("添加成员失败，可能是因为记录已存在");
        }
    }
} 