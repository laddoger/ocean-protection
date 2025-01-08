CREATE TABLE marine_animal_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL COMMENT '类别名称',
    description TEXT COMMENT '类别描述',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT(1) DEFAULT 0
);

CREATE TABLE marine_animal (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    category_id BIGINT NOT NULL COMMENT '类别ID',
    name VARCHAR(100) NOT NULL COMMENT '动物名称',
    scientific_name VARCHAR(100) COMMENT '学名',
    cover_image VARCHAR(255) COMMENT '封面图片URL',
    description TEXT COMMENT '详细描述',
    habitat TEXT COMMENT '栖息地',
    conservation_status VARCHAR(50) COMMENT '保护状态',
    featured TINYINT(1) DEFAULT 0 COMMENT '是否精选',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT(1) DEFAULT 0
); 