-- 删除旧表（如果存在）
DROP TABLE IF EXISTS marine_animal;

-- 创建新的海洋动物表
CREATE TABLE marine_animal (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL COMMENT '动物名称',
    scientific_name VARCHAR(100) COMMENT '动物学名',
    category VARCHAR(50) NOT NULL DEFAULT '未分类' COMMENT '动物类别',
    description TEXT COMMENT '动物简介',
    conservation_status VARCHAR(50) COMMENT '保护状态',
    habitat TEXT COMMENT '栖息地',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除',
    INDEX idx_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='海洋动物信息表';

-- 插入一些初始数据
INSERT INTO marine_animal 
(name, scientific_name, category, description, conservation_status, habitat) 
VALUES 
('蓝鲸', 'Balaenoptera musculus', '鲸类', 
 '蓝鲸是地球上最大的动物，可以长到33米长，重达200吨。它们主要以磷虾为食，尽管体型庞大，但性情温和。',
 '濒危', '深海水域，主要分布在南极洲和北极周边海域'),

('绿海龟', 'Chelonia mydas', '海龟',
 '绿海龟是最大的硬壳海龟之一，以其橄榄绿色的脂肪而得名。它们主要以海草和海藻为食。',
 '濒危', '热带和亚热带海域，在浅海区域觅食，在海滩产卵'),

('珊瑚', 'Anthozoa', '珊瑚类',
 '珊瑚是海洋生态系统的重要组成部分，为众多海洋生物提供栖息地和庇护所。',
 '易危', '热带浅海区域，需要充足的阳光和清澈的水质'),

('海豚', 'Delphinidae', '鲸类',
 '海豚是一种聪明的海洋哺乳动物，具有高度的智力和社交能力，常常成群活动。',
 '易危', '世界各大洋的表层水域，既有近海也有远洋种类'),

('座头鲸', 'Megaptera novaeangliae', '鲸类',
 '座头鲸以其独特的歌声和跃出水面的行为而闻名，每年会进行长距离迁徙。',
 '易危', '全球海域，从极地到热带水域都有分布'); 