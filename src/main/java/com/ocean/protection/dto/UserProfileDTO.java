package com.ocean.protection.dto;

import com.ocean.protection.entity.ForumPost;
import com.ocean.protection.entity.VolunteerOrganization;
import com.ocean.protection.entity.VolunteerActivity;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class UserProfileDTO {
    private Long id;
    private String username;
    private String gender;
    private Integer age;
    private String address;
    private List<ForumPost> posts;
    private List<VolunteerOrganization> organizations;
    private List<VolunteerActivity> activities;
} 