package com.ocean.protection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ocean.protection.dto.LoginDTO;
import com.ocean.protection.dto.RegisterDTO;
import com.ocean.protection.entity.User;
import com.ocean.protection.mapper.UserMapper;
import com.ocean.protection.service.UserService;
import com.ocean.protection.util.JwtUtil;
import com.ocean.protection.dto.LoginResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import io.jsonwebtoken.Claims;
import com.ocean.protection.dto.UserProfileDTO;
import com.ocean.protection.dto.UpdateProfileDTO;
import com.ocean.protection.entity.ForumPost;
import com.ocean.protection.entity.OrganizationMember;
import com.ocean.protection.entity.ActivityParticipant;
import com.ocean.protection.entity.VolunteerOrganization;
import com.ocean.protection.entity.VolunteerActivity;
import com.ocean.protection.mapper.ForumPostMapper;
import com.ocean.protection.mapper.OrganizationMemberMapper;
import com.ocean.protection.mapper.ActivityParticipantMapper;
import com.ocean.protection.mapper.VolunteerOrganizationMapper;
import com.ocean.protection.mapper.VolunteerActivityMapper;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final ForumPostMapper postMapper;
    private final OrganizationMemberMapper organizationMemberMapper;
    private final ActivityParticipantMapper activityParticipantMapper;
    private final VolunteerOrganizationMapper organizationMapper;
    private final VolunteerActivityMapper activityMapper;

    @Override
    public LoginResponseDTO login(LoginDTO loginDTO) {
        log.info("尝试登录用户: {}", loginDTO.getUsername());
        
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, loginDTO.getUsername());
        
        User user = userMapper.selectOne(wrapper);
        log.info("查询到的用户: {}", user);
        
        if (user == null || !loginDTO.getPassword().equals(user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        log.info("生成的token: {}", token);
        
        return LoginResponseDTO.of(token, user);
    }

    @Override
    @Transactional
    public User register(RegisterDTO registerDTO) {
        // 检查用户名是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, registerDTO.getUsername());
        if (userMapper.selectOne(wrapper) != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 创建新用户
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        // 直接使用明文密码
        user.setPassword(registerDTO.getPassword());
        
        userMapper.insert(user);
        return user;
    }

    @Override
    public User getCurrentUser() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader("Authorization");
        
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            Claims claims = jwtUtil.parseToken(token);
            if (claims != null) {
                Long userId = claims.get("userId", Long.class);
                return userMapper.selectById(userId);
            }
        }
        return null;
    }

    @Override
    public UserProfileDTO getUserProfile(Long userId) {
        // 获取用户基本信息
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        UserProfileDTO profileDTO = new UserProfileDTO();
        BeanUtils.copyProperties(user, profileDTO);

        // 获取用户发表的帖子
        LambdaQueryWrapper<ForumPost> postWrapper = new LambdaQueryWrapper<>();
        postWrapper.eq(ForumPost::getUserId, userId)
                  .eq(ForumPost::getDeleted, false)
                  .orderByDesc(ForumPost::getCreatedTime);
        profileDTO.setPosts(postMapper.selectList(postWrapper));

        // 获取用户加入的组织
        List<Long> organizationIds = organizationMemberMapper.selectList(
            new LambdaQueryWrapper<OrganizationMember>()
                .eq(OrganizationMember::getUserId, userId)
        ).stream().map(OrganizationMember::getOrganizationId).collect(Collectors.toList());
        
        if (!organizationIds.isEmpty()) {
            profileDTO.setOrganizations(organizationMapper.selectBatchIds(organizationIds));
        }

        // 获取用户参加的活动
        List<Long> activityIds = activityParticipantMapper.selectList(
            new LambdaQueryWrapper<ActivityParticipant>()
                .eq(ActivityParticipant::getUserId, userId)
        ).stream().map(ActivityParticipant::getActivityId).collect(Collectors.toList());
        
        if (!activityIds.isEmpty()) {
            profileDTO.setActivities(activityMapper.selectBatchIds(activityIds));
        }

        return profileDTO;
    }

    @Override
    @Transactional
    public void updateProfile(Long userId, UpdateProfileDTO updateProfileDTO) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        user.setGender(updateProfileDTO.getGender());
        user.setAge(updateProfileDTO.getAge());
        user.setAddress(updateProfileDTO.getAddress());

        userMapper.updateById(user);
    }

    @Override
    @Transactional
    public User updateUserInfo(Long userId, String gender, Integer age, String address) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 验证数据
        if (age != null && (age < 0 || age > 150)) {
            throw new RuntimeException("年龄必须在0-150之间");
        }
        if (gender != null && !Arrays.asList("男", "女", "未设置").contains(gender)) {
            throw new RuntimeException("性别只能是'男'、'女'或'未设置'");
        }
        
        // 只更新允许修改的字段
        if (gender != null) user.setGender(gender);
        if (age != null) user.setAge(age);
        if (address != null) user.setAddress(address);
        
        userMapper.updateById(user);
        return user;
    }
} 