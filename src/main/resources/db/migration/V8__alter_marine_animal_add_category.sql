-- 为 marine_animal 表添加 category 字段
ALTER TABLE marine_animal
ADD COLUMN category VARCHAR(50) DEFAULT '未分类' COMMENT '动物类别';

-- 更新现有数据的类别
UPDATE marine_animal SET category = '鲸类' WHERE name LIKE '%鲸%' OR name LIKE '%海豚%';
UPDATE marine_animal SET category = '鱼类' WHERE name LIKE '%鱼%';
UPDATE marine_animal SET category = '海龟' WHERE name LIKE '%龟%';
UPDATE marine_animal SET category = '海豹类' WHERE name LIKE '%海豹%' OR name LIKE '%海狮%';
UPDATE marine_animal SET category = '珊瑚类' WHERE name LIKE '%珊瑚%';
UPDATE marine_animal SET category = '软体动物' WHERE name LIKE '%章鱼%' OR name LIKE '%鱿鱼%' OR name LIKE '%贝%'; 