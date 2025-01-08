package com.ocean.protection.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ocean.protection.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
} 