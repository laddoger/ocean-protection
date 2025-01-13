-- 检查表是否存在，如果不存在则创建
CREATE TABLE IF NOT EXISTS volunteer_organization (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    founder_id BIGINT NOT NULL,
    member_count INT DEFAULT 0,
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT(1) DEFAULT 0
);

CREATE TABLE IF NOT EXISTS volunteer_activity (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    organization_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    location VARCHAR(200),
    start_time DATETIME,
    end_time DATETIME,
    status VARCHAR(20) DEFAULT 'ONGOING',
    participant_count INT DEFAULT 0,
    max_participants INT,
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT(1) DEFAULT 0
);

-- 组织成员表
CREATE TABLE organization_member (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    organization_id BIGINT NOT NULL COMMENT '组织ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role VARCHAR(20) DEFAULT 'MEMBER' COMMENT '角色：FOUNDER-创建者，ADMIN-管理员，MEMBER-成员',
    join_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT(1) DEFAULT 0,
    UNIQUE KEY `uk_org_user` (`organization_id`, `user_id`)
);

-- 活动参与表
CREATE TABLE activity_participant (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    activity_id BIGINT NOT NULL COMMENT '活动ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    join_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT(1) DEFAULT 0,
    UNIQUE KEY `uk_activity_user` (`activity_id`, `user_id`)
);

-- 初始化示例数据
INSERT INTO volunteer_organization (name, description, founder_id, member_count) VALUES 
('海洋守护者联盟', '致力于保护海洋生态环境，组织海滩清理和海洋生物救助活动', 1, 1),
('蔚蓝地球保护协会', '关注海洋污染问题，开展环保教育和科研活动', 1, 1),
('珊瑚礁保护志愿者', '专注于珊瑚礁生态系统的保护和修复工作', 1, 1);

INSERT INTO volunteer_activity (organization_id, title, description, location, start_time, end_time, status, participant_count, max_participants) VALUES 
(1, '2024世界海洋日海滩清理行动', '组织志愿者进行海滩垃圾清理，提高公众环保意识', '青岛第一海水浴场', '2024-06-08 09:00:00', '2024-06-08 17:00:00', 'ONGOING', 0, 50),
(1, '受伤海龟救助培训', '开展海龟救助知识培训，提高志愿者专业救助能力', '青岛海洋大学海洋生物研究中心', '2024-05-15 14:00:00', '2024-05-15 17:00:00', 'ONGOING', 0, 30),
(2, '海洋生物多样性调查', '开展海岸线生物多样性调查，记录和统计海洋生物种类', '威海刘公岛周边海域', '2024-07-01 08:00:00', '2024-07-03 18:00:00', 'ONGOING', 0, 20),
(2, '海洋环保科普讲座', '面向中小学生开展海洋环保知识普及活动', '青岛海洋科技馆', '2024-04-20 14:00:00', '2024-04-20 16:00:00', 'ONGOING', 0, 100),
(3, '珊瑚礁生态修复行动', '组织专业志愿者进行珊瑚礁修复和监测工作', '三亚鹿回头海域', '2024-08-15 09:00:00', '2024-08-20 17:00:00', 'ONGOING', 0, 15);

-- 初始化组织创建者为成员
INSERT INTO organization_member (organization_id, user_id, role) VALUES 
(1, 1, 'FOUNDER'),
(2, 1, 'FOUNDER'),
(3, 1, 'FOUNDER'); 