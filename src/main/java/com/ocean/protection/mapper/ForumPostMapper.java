package com.ocean.protection.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ocean.protection.entity.ForumPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface ForumPostMapper extends BaseMapper<ForumPost> {
    
    @Select("SELECT p.id, p.user_id, p.title, p.content, p.view_count, " +
           "p.like_count, p.comment_count, p.created_time, p.updated_time, p.deleted " +
           "FROM post p " +
           "WHERE p.deleted = 0 " +
           "ORDER BY p.created_time DESC")
    List<ForumPost> selectAllPosts();
} 