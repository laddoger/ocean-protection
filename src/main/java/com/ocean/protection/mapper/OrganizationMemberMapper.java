package com.ocean.protection.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ocean.protection.entity.OrganizationMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrganizationMemberMapper extends BaseMapper<OrganizationMember> {
    Integer countMember(@Param("organizationId") Long organizationId, @Param("userId") Long userId);
    void insertMember(OrganizationMember member);
} 