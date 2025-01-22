package com.ocean.protection.service.impl;

import com.ocean.protection.mapper.OrganizationMapper;
import com.ocean.protection.mapper.OrganizationMemberMapper;
import com.ocean.protection.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DuplicateKeyException;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {
    
    private final OrganizationMapper organizationMapper;
    private final OrganizationMemberMapper memberMapper;

    @Override
    public boolean isMember(Long organizationId, Long userId) {
        return memberMapper.countMember(organizationId, userId) > 0;
    }

    @Override
    public void removeMember(Long organizationId, Long userId) {
        memberMapper.deleteMember(organizationId, userId);
    }

    @Override
    @Transactional
    public void addMember(Long organizationId, Long userId) {
        // 先检查用户是否已经是成员
        if (isMember(organizationId, userId)) {
            throw new ServiceException("用户已经是该组织成员");
        }
        
        // 检查组织是否存在
        Organization org = organizationMapper.selectById(organizationId);
        if (org == null || org.getDeleted()) {
            throw new ServiceException("组织不存在");
        }
        
        // 添加成员
        OrganizationMember member = new OrganizationMember();
        member.setOrganizationId(organizationId);
        member.setUserId(userId);
        member.setRole("MEMBER");
        
        try {
            memberMapper.insert(member);
            
            // 更新组织成员数量
            org.setMemberCount(org.getMemberCount() + 1);
            organizationMapper.updateById(org);
        } catch (DuplicateKeyException e) {
            // 如果是因为记录已存在但被标记为删除，则恢复该记录
            memberMapper.deleteMember(organizationId, userId);  // 这里的删除实际是设置 deleted = 0
        }
    }
} 