package com.ocean.protection.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ocean.protection.entity.ActivityParticipant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ActivityParticipantMapper extends BaseMapper<ActivityParticipant> {
    Integer countParticipant(@Param("activityId") Long activityId, 
                           @Param("userId") Long userId);
                           
    void insertParticipant(ActivityParticipant participant);
} 