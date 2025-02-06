package com.ocean.protection.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@TableName("volunteer_activity")
public class VolunteerActivity {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("organization_id")
    private Long organizationId;
    
    private String title;
    
    private String description;
    
    private String location;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("start_time")
    private LocalDateTime startTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("end_time")
    private LocalDateTime endTime;
    
    @TableField("participant_count")
    private Integer participantCount;
    
    @TableField("max_participants")
    private Integer maxParticipants;
    
    private String status;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
    
    @TableLogic
    private Boolean deleted;
    
    @TableField(exist = false)
    private VolunteerOrganization organization;
    
    @TableField(exist = false)
    private List<User> participants;
    
    @TableField(exist = false)
    private Boolean isParticipant;
    
    public Boolean getIsParticipant() {
        return isParticipant;
    }
    
    public void setIsParticipant(Boolean isParticipant) {
        this.isParticipant = isParticipant;
    }
} 