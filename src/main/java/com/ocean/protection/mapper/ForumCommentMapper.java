package com.ocean.protection.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ocean.protection.entity.ForumComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface ForumCommentMapper extends BaseMapper<ForumComment> {
    
    @Select("SELECT c.id, c.post_id, c.user_id, c.content, c.created_time, c.deleted " +
           "FROM post_comment c " +
           "WHERE c.post_id = #{postId} AND c.deleted = 0 " +
           "ORDER BY c.created_time DESC")
    List<ForumComment> selectByPostId(Long postId);
} 